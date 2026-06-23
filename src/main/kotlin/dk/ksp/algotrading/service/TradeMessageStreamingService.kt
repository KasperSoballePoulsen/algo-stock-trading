package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoStreamingClient
import org.springframework.stereotype.Service

@Service
class TradeMessageStreamingService(
    private val saxoStreamingClient: SaxoStreamingClient
) {
    private val contextId = "algo-trading-app"

    fun connect() {
        saxoStreamingClient.openWebsocket(
            contextId = contextId,
            onConnected = {
                saxoStreamingClient.createTradeMessageSubscription(contextId)
            },
            onMessage = ::handleMessage
        )
    }

    private fun handleMessage(message: TradeMessageDTO) {
        println(message)
        // save to DB
        // send websocket notification to frontend
        // update order status
    }
}