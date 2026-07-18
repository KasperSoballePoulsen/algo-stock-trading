package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoStreamingClient
import dk.ksp.algotrading.dto.saxo.response.SaxoOrderEventDTO
import dk.ksp.algotrading.dto.saxo.response.SaxoStreamEvent
import dk.ksp.algotrading.dto.saxo.response.SaxoTradeMessageDTO
import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.OrderType
import dk.ksp.algotrading.enum.SaxoEventActivity
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import dk.ksp.algotrading.event.SaxoAccessTokenRefreshedEvent
import org.springframework.context.event.EventListener

@Service
class StreamingService(
    private val saxoStreamingClient: SaxoStreamingClient,
    private val notificationService: NotificationService,
    private val tradingService: TradingService
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val connected = AtomicBoolean(false)
    private val connecting = AtomicBoolean(false)
    private val reconnectScheduled = AtomicBoolean(false)
    private val shuttingDown = AtomicBoolean(false)
    private val reconnectExecutor = Executors.newSingleThreadScheduledExecutor()


    fun connect() {

        if (shuttingDown.get() || connected.get()) return

        if (!connecting.compareAndSet(false, true)) return


        logger.info("Connecting to Saxo stream")

        saxoStreamingClient.openWebsocket(
            onConnected = {
                connecting.set(false)
                connected.set(true)
                reconnectScheduled.set(false)

                try {
                    createSubscriptions()
                    logger.info("Connected to Saxo stream")
                } catch (error: Exception) {
                    logger.error(
                        "Connected to Saxo stream, but subscription creation failed",
                        error
                    )
                    connected.set(false)
                    saxoStreamingClient.close()
                    scheduleReconnect(error)
                }
            },
            onDisconnected = { error ->
                connected.set(false)
                connecting.set(false)

                scheduleReconnect(error)
            },
            onMessage = ::handleMessages
        )
    }

    private fun createSubscriptions() {
        saxoStreamingClient.createTradeMessageSubscription("trade-messages")

        saxoStreamingClient.createClientEventsSubscription(
            "order-detail-messages",
            listOf(SaxoEventActivity.ORDERS)
        )
    }

    private fun scheduleReconnect(error: Throwable?) {
        if (shuttingDown.get()) return

        if (!reconnectScheduled.compareAndSet(false, true)) return

        logger.warn("Saxo stream disconnected. Reconnecting in 10 seconds", error
        )

        reconnectExecutor.schedule(
            {
                reconnectScheduled.set(false)
                connect()
            },
            10,
            TimeUnit.SECONDS
        )
    }

    private fun handleMessages(messages: List<SaxoStreamEvent>) {
        if (messages.isEmpty()) return

        messages.forEach { message ->
            when (message) {
                is SaxoOrderEventDTO -> handleOrderEvent(message)
                is SaxoTradeMessageDTO -> handleTradeMessage(message)
            }
        }
    }

    private fun handleOrderEvent(orderEvent: SaxoOrderEventDTO) {
        tradingService.updateOrder(
            orderEvent.orderId,
            OrderStatus.fromSaxoValue(orderEvent.status),
            OrderType.fromSaxoValue(orderEvent.orderType),
            orderEvent.amount.toLong(),
            orderEvent.executionPrice
        )
    }


    private fun handleTradeMessage(tradeMessage: SaxoTradeMessageDTO) {
        saxoStreamingClient.markMessagesAsSeen(listOf(tradeMessage.messageId))
        notificationService.sendNotification(tradeMessage.messageBody, tradeMessage.messageHeader)
    }

    @PreDestroy
    fun shutdown() {
        logger.info("Shutting down Saxo streaming connection")

        shuttingDown.set(true)
        reconnectExecutor.shutdownNow()
        saxoStreamingClient.close()
    }

    @EventListener
    fun onAccessTokenRefreshed(event: SaxoAccessTokenRefreshedEvent) {
        if (!connected.get() || shuttingDown.get()) return

        try {
            saxoStreamingClient.authorizeStreamingContext(event.accessToken)
        } catch (error: Exception) {
            logger.error("Could not re-authorize Saxo stream", error)
            saxoStreamingClient.close()
        }
    }

}