package dk.ksp.algotrading.streaming

import dk.ksp.algotrading.dto.saxo.response.SaxoTradeMessageDTO
import org.slf4j.LoggerFactory
import java.net.http.WebSocket
import java.nio.ByteBuffer
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

class SaxoWebSocketListener(
    private val messageParser: SaxoStreamMessageParser,
    private val onConnected: () -> Unit,
    private val onMessage: (List<SaxoTradeMessageDTO>) -> Unit
) : WebSocket.Listener {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onOpen(webSocket: WebSocket) {
        webSocket.request(1)
        onConnected()
    }

    override fun onBinary(
        webSocket: WebSocket,
        data: ByteBuffer,
        last: Boolean
    ): CompletionStage<*> {
        val messages = messageParser.parse(data)

        if (messages.isNotEmpty()) {
            onMessage(messages)
        }
        webSocket.request(1)
        return CompletableFuture.completedFuture(null)
    }

    override fun onError(webSocket: WebSocket, error: Throwable) {
        logger.error("Saxo WebSocket error: ${error.message}")
    }
}