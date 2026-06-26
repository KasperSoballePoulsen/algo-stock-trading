package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoStreamingClient
import dk.ksp.algotrading.dto.saxo.response.SaxoOrderEventDTO
import dk.ksp.algotrading.dto.saxo.response.SaxoStreamEvent
import dk.ksp.algotrading.dto.saxo.response.SaxoTradeMessageDTO
import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.SaxoEventActivity
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Service

@Service
class StreamingService(
    private val saxoStreamingClient: SaxoStreamingClient,
    private val notificationService: NotificationService,
    private val tradingService: TradingService
) {

    fun connect() {


        saxoStreamingClient.openWebsocket(
            onConnected = {
                saxoStreamingClient.createTradeMessageSubscription("trade-messages")
                saxoStreamingClient.createClientEventsSubscription(
                    "Order-detail-messages",
                    listOf(SaxoEventActivity.ORDERS)
                )
            },
            onMessage = ::handleMessages
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
            orderEvent.executionPrice
        )
    }


    private fun handleTradeMessage(tradeMessage: SaxoTradeMessageDTO) {
        saxoStreamingClient.markMessagesAsSeen(listOf(tradeMessage.messageId))
        notificationService.sendNotification(tradeMessage.messageBody, tradeMessage.messageHeader)
    }

    @PreDestroy
    fun shutdown() {
        saxoStreamingClient.close()
    }
}