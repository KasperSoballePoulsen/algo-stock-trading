package dk.ksp.algotrading.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class MarketDataClient(

    @Value("\${saxo.base-url}")
    private val baseUrl: String,

    @Value("\${saxo.access-token}")
    private val accessToken: String
) {

    private val client =
        HttpClient.newHttpClient()

    fun fetchInstruments(): String {

        val request =
            HttpRequest
                .newBuilder()
                .uri(
                    URI.create(
                        "$baseUrl/ref/v1/instruments?KeyWords=DKK&AssetTypes=FxSpot"
                    )
                )
                .header(
                    "Authorization",
                    "Bearer $accessToken"
                )
                .GET()
                .build()

        val response =
            client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            )

        println("status code: " + response.statusCode())

        return response.body()
    }
}