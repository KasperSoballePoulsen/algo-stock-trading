package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoStreamingClient
import dk.ksp.algotrading.dto.saxo.response.SaxoTradeMessageDTO
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Service

@Service
class TradeMessageStreamingService(
    private val saxoStreamingClient: SaxoStreamingClient,
    private val notificationService: NotificationService,
    private val tradingService: TradingService
) {

    fun connect() {


        saxoStreamingClient.openWebsocket(
            onConnected = {
                saxoStreamingClient.createTradeMessageSubscription("trade-messages")
            },
            onMessage = ::handleMessages
        )
    }

    private fun handleMessages(messages: List<SaxoTradeMessageDTO>) {
        if (messages.isEmpty()) return

        messages.forEach { message ->
            when (message.messageHeader) {
                "Trade confirmation" -> {
                    val sourceOrderId = message.sourceOrderId
                        ?: throw IllegalStateException("Trade confirmation message missing SourceOrderId")

                    tradingService.handleOrderFilled(sourceOrderId)
                }
            }

            notificationService.sendNotification(
                message.messageBody,
                message.messageHeader
            )
        }

        val messageIds = messages.map { it.messageId }
        saxoStreamingClient.markMessagesAsSeen(messageIds)
    }

    @PreDestroy
    fun shutdown() {
        saxoStreamingClient.close()
    }
}