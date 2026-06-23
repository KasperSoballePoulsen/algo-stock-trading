package dk.ksp.algotrading.streaming

import java.net.http.WebSocket
import java.nio.ByteBuffer
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

class SaxoWebSocketListener(
    private val messageParser: SaxoStreamMessageParser,
    private val onConnected: () -> Unit,
    private val onMessage: (TradeMessageDTO) -> Unit
) : WebSocket.Listener {

    override fun onOpen(webSocket: WebSocket) {
        println("Connected to Saxo stream")
        webSocket.request(1)
        onConnected()
    }

//    override fun onText(
//        webSocket: WebSocket,
//        data: CharSequence,
//        last: Boolean
//    ): CompletionStage<*> {
//        messageHandler.handleText(data.toString())
//        webSocket.request(1)
//        return CompletableFuture.completedFuture(null)
//    }

    override fun onBinary(
        webSocket: WebSocket,
        data: ByteBuffer,
        last: Boolean
    ): CompletionStage<*> {
        val message = messageParser.parse(data)

        if (message != null) {
            onMessage(message)
        }
        webSocket.request(1)
        return CompletableFuture.completedFuture(null)
    }

    override fun onError(webSocket: WebSocket, error: Throwable) {
        println("Saxo WebSocket error: ${error.message}")
    }
}