package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoStreamingClient
import dk.ksp.algotrading.dto.saxo.response.SaxoTradeMessageDTO
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Service

@Service
class TradeMessageStreamingService(
    private val saxoStreamingClient: SaxoStreamingClient,
    private val notificationService: NotificationService
) {

    fun connect() {
        val contextId = "algo-trading-app"

        saxoStreamingClient.openWebsocket(
            contextId = contextId,
            onConnected = {
                saxoStreamingClient.createTradeMessageSubscription(contextId, "trade-messages")
            },
            onMessage = ::handleMessages
        )
    }

    private fun handleMessages(messages: List<SaxoTradeMessageDTO>) {
        if (messages.isEmpty()) return

        messages.forEach { notificationService.sendNotification(it.messageBody, it.messageHeader) }

        val messageIds = messages.map { it.messageId }

        saxoStreamingClient.markMessagesAsSeen(messageIds)
        // save to DB
        // send websocket notification to frontend
        // update order status
    }

    @PreDestroy
    fun shutdown() {
        saxoStreamingClient.close()
    }
}