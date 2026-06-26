package dk.ksp.algotrading.client

import com.fasterxml.jackson.databind.ObjectMapper
import dk.ksp.algotrading.dto.saxo.request.SaxoClientEventsSubscriptionArgumentsDTO
import dk.ksp.algotrading.dto.saxo.request.SaxoClientEventsSubscriptionDTO
import dk.ksp.algotrading.dto.saxo.request.SaxoTradeMessageSubscriptionRequestDTO
import dk.ksp.algotrading.dto.saxo.response.SaxoStreamEvent
import dk.ksp.algotrading.dto.saxo.response.SaxoTradeMessageDTO
import dk.ksp.algotrading.enum.SaxoEventActivity
import dk.ksp.algotrading.streaming.SaxoStreamMessageParser
import dk.ksp.algotrading.streaming.SaxoWebSocketListener
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.WebSocket

@Component
class SaxoStreamingClient(
    @Value("\${saxo-sim-api.api-token}")
    private val saxoToken: String,
    @Value("\${saxo-sim-api.base-url}")
    private val baseUrl: String,
    private val objectMapper: ObjectMapper,
    private val client: HttpClient,
    private val messageParser: SaxoStreamMessageParser

) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var webSocket: WebSocket? = null
    val contextId = "algo-trading-app"


    fun createTradeMessageSubscription(referenceId: String) {
        val requestBody = SaxoTradeMessageSubscriptionRequestDTO(contextId, referenceId)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/trade/v1/messages/subscriptions"))
            .header("Authorization", "Bearer $saxoToken")
            .header("Content-Type", "application/json; charset=utf-8")
            .POST(
                HttpRequest.BodyPublishers.ofString(
                    objectMapper.writeValueAsString(requestBody)
                )
            )
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() !in 200..299) {
            throw IllegalStateException(
                "Failed to create Saxo trade message subscription. Status=${response.statusCode()}, Body=${response.body()}"
            )
        }

    }

    fun createClientEventsSubscription(
        referenceId: String,
        saxoActivities: List<SaxoEventActivity>
    ) {
        val requestBody = SaxoClientEventsSubscriptionDTO(
            SaxoClientEventsSubscriptionArgumentsDTO(saxoActivities), contextId, referenceId
        )

        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/ens/v1/activities/subscriptions"))
            .header("Authorization", "Bearer $saxoToken")
            .header("Content-Type", "application/json; charset=utf-8")
            .POST(
                HttpRequest.BodyPublishers.ofString(
                    objectMapper.writeValueAsString(requestBody)
                )
            )
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() !in 200..299) {
            throw IllegalStateException(
                "Failed to create Saxo client event subscription. Status=${response.statusCode()}, Body=${response.body()}"
            )
        }


    }

    fun openWebsocket(onConnected: () -> Unit, onMessage: (List<SaxoStreamEvent>) -> Unit) {
        val uri = URI.create("wss://sim-streaming.saxobank.com/sim/oapi/streaming/ws/connect?contextId=$contextId")
        client.newWebSocketBuilder()
            .header("Authorization", "Bearer $saxoToken")
            .buildAsync(uri, SaxoWebSocketListener(messageParser, onConnected, onMessage))
            .thenAccept { webSocket = it }
            .exceptionally {
                logger.error("Could not connect to Saxo stream", it)
                null
            }
    }

    fun close() {
        webSocket?.sendClose(WebSocket.NORMAL_CLOSURE, "Closing")
        webSocket = null
    }

    fun markMessagesAsSeen(messageIds: List<String>) {
        if (messageIds.isEmpty()) return

        val joinedMessageIds = messageIds.joinToString(",")

        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/trade/v1/messages/seen?MessageIds=$joinedMessageIds"))
            .header("Authorization", "Bearer $saxoToken")
            .PUT(HttpRequest.BodyPublishers.noBody())
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() !in 200..299) {
            throw IllegalStateException(
                "Failed to mark Saxo trade messages as seen. Status=${response.statusCode()}, Body=${response.body()}"
            )
        }
    }

}