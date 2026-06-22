package dk.ksp.algotrading.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dk.ksp.algotrading.dto.marketdata.response.QuoteDataDTO
import dk.ksp.algotrading.exception.MarketDataException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class MarketDataClient(
    @Value("\${finnhub.base-url}")
    private val baseUrl: String,
    @Value("\${finnhub.api-key}")
    private val apiKey: String
) {
    private val client = HttpClient.newHttpClient()
    private val objectMapper = jacksonObjectMapper()

    fun fetchRealTimeQuoteData(symbol: String): QuoteDataDTO {
        try {
            val request = HttpRequest
                .newBuilder()
                .uri(URI.create("$baseUrl/quote?symbol=$symbol"))
                .header("X-Finnhub-Token", apiKey)
                .GET()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() != 200) {
                throw RuntimeException(
                    "Finnhub request failed. Status=${response.statusCode()}, Body=${response.body()}"
                )
            }

            return objectMapper.readValue<QuoteDataDTO>(response.body())
        } catch (e: Exception) {
            throw MarketDataException("Failed to fetch quote for symbol=$symbol", e)
        }
    }
}