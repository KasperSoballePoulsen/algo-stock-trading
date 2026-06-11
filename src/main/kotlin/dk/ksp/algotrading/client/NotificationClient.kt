package dk.ksp.algotrading.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class NotificationClient(
    @Value("\${ntfy.topic-name}")
    private val topicName: String,
    @Value("\${ntfy.base-url}")
    private val baseUrl: String,
) {
    private val client = HttpClient.newHttpClient()

    fun sendNotification(message: String, title: String) {

        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/$topicName"))
            .header("Title", title)
            .POST(HttpRequest.BodyPublishers.ofString(message))
            .build()

        client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}