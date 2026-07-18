package dk.ksp.algotrading.client

import com.fasterxml.jackson.databind.ObjectMapper
import dk.ksp.algotrading.dto.saxo.request.SaxoClientEventsSubscriptionArgumentsDTO
import dk.ksp.algotrading.dto.saxo.request.SaxoClientEventsSubscriptionDTO
import dk.ksp.algotrading.dto.saxo.request.SaxoTradeMessageSubscriptionRequestDTO
import dk.ksp.algotrading.dto.saxo.response.SaxoStreamEvent
import dk.ksp.algotrading.enum.SaxoEventActivity
import dk.ksp.algotrading.service.SaxoTokenService
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
    @Value("\${saxo-sim-api.base-url}")
    private val baseUrl: String,
    @Value("\${saxo-sim-api.streaming-url}")
    private val streamingUrl: String,
    private val objectMapper: ObjectMapper,
    private val httpClient: HttpClient,
    private val messageParser: SaxoStreamMessageParser,
    private val saxoTokenService: SaxoTokenService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var webSocket: WebSocket? = null
    val contextId = "algo-trading-app"
    private val authorizationHeader: String get() = "Bearer ${saxoTokenService.getValidAccessToken()}"


    fun createTradeMessageSubscription(referenceId: String) {
        val requestBody = SaxoTradeMessageSubscriptionRequestDTO(contextId, referenceId)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/trade/v1/messages/subscriptions"))
            .header("Authorization", authorizationHeader)
            .header("Content-Type", "application/json; charset=utf-8")
            .POST(
                HttpRequest.BodyPublishers.ofString(
                    objectMapper.writeValueAsString(requestBody)
                )
            )
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

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
            .header("Authorization", authorizationHeader)
            .header("Content-Type", "application/json; charset=utf-8")
            .POST(
                HttpRequest.BodyPublishers.ofString(
                    objectMapper.writeValueAsString(requestBody)
                )
            )
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() !in 200..299) {
            throw IllegalStateException(
                "Failed to create Saxo client event subscription. Status=${response.statusCode()}, Body=${response.body()}"
            )
        }


    }

    fun openWebsocket(onConnected: () -> Unit, onDisconnected: (Throwable?) -> Unit, onMessage: (List<SaxoStreamEvent>) -> Unit) {
        val uri = URI.create("wss://sim-streaming.saxobank.com/sim/oapi/streaming/ws/connect?contextId=$contextId")
        httpClient.newWebSocketBuilder()
            .header("Authorization", authorizationHeader)
            .buildAsync(uri, SaxoWebSocketListener(messageParser, onConnected, onDisconnected, onMessage))
            .thenAccept { webSocket = it }
            .exceptionally { error ->
                logger.error("Could not connect to Saxo stream", error)
                onDisconnected(error)
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
            .header("Authorization", authorizationHeader)
            .PUT(HttpRequest.BodyPublishers.noBody())
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() !in 200..299) {
            throw IllegalStateException(
                "Failed to mark Saxo trade messages as seen. Status=${response.statusCode()}, Body=${response.body()}"
            )
        }
    }


    fun authorizeStreamingContext(accessToken: String) {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$streamingUrl/authorize?contextid=$contextId"))
            .header("Authorization", "Bearer $accessToken")
            .PUT(HttpRequest.BodyPublishers.noBody())
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 202) {
            throw IllegalStateException(
                "Failed to re-authorize Saxo stream. Status=${response.statusCode()}, body=${response.body()}"
            )
        }

        logger.info("Re-authorized Saxo streaming context")
    }

}