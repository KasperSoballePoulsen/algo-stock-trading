package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoStreamingClient
import dk.ksp.algotrading.dto.saxo.response.SaxoTradeMessageDTO
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
            onMessage = ::handleMessages
        )
    }

    private fun handleMessages(messages: List<SaxoTradeMessageDTO>) {
        println("Sending out messages here")

        //println(message)
        // save to DB
        // send websocket notification to frontend
        // update order status
    }
}