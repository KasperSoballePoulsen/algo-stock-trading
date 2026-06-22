package dk.ksp.algotrading.streaming

import dk.ksp.algotrading.client.SaxoStreamingClient
import org.springframework.stereotype.Service

@Service
class TradeMessageListener(
    private val saxoStreamingClient: SaxoStreamingClient
) {
    private val contextId = "algo-trading-app"
    fun connect() {

        saxoStreamingClient.openWebsocket(contextId) {
            saxoStreamingClient.createTradeMessageSubscription(contextId)
        }
    }

}