package dk.ksp.algotrading.streaming

import dk.ksp.algotrading.dto.saxo.response.SaxoStreamEvent
import dk.ksp.algotrading.dto.saxo.response.SaxoTradeMessageDTO
import org.slf4j.LoggerFactory
import java.net.http.WebSocket
import java.nio.ByteBuffer
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.atomic.AtomicBoolean

class SaxoWebSocketListener(
    private val messageParser: SaxoStreamMessageParser,
    private val onConnected: () -> Unit,
    private val onDisconnected: (Throwable?) -> Unit,
    private val onMessage: (List<SaxoStreamEvent>) -> Unit,
) : WebSocket.Listener {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onOpen(webSocket: WebSocket) {
        webSocket.request(1)
        onConnected()
    }

    override fun onBinary(webSocket: WebSocket, data: ByteBuffer, last: Boolean): CompletionStage<*> {
        val messages = messageParser.parse(data)

        if (messages.isNotEmpty()) {
            onMessage(messages)
        }
        webSocket.request(1)
        return CompletableFuture.completedFuture(null)
    }

    override fun onError(webSocket: WebSocket, error: Throwable) {
        logger.error("Saxo WebSocket error", error)
        onDisconnected(error)
    }

    override fun onClose(webSocket: WebSocket, statusCode: Int, reason: String): CompletionStage<*> {
        logger.warn("Saxo WebSocket closed. Status={}, reason={}", statusCode, reason)

        onDisconnected(null)
        return CompletableFuture.completedFuture(null)
    }
}