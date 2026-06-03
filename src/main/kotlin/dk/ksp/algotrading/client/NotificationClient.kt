package dk.ksp.algotrading.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class NotificationClient(
    @Value("\${ntfy.stock-topic-name}")
    private val stockTopicName: String
) {
    private val client = HttpClient.newHttpClient()

    fun sendNotification(message: String, title: String) {

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://ntfy.sh/$stockTopicName"))
            .header("Title", title)
            .POST(HttpRequest.BodyPublishers.ofString(message))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 200) {
            println("Fail")
        } else {
            println("Success")
        }
    }
}