package dk.ksp.algotrading.client

import com.fasterxml.jackson.databind.ObjectMapper
import dk.ksp.algotrading.dto.response.SaxoClientDTO
import dk.ksp.algotrading.entity.TradingAccount
import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.OrderType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import com.fasterxml.jackson.module.kotlin.readValue

@Component
class BrokerClient(
    @Value("\${saxo-sim-api.api-token}")
    private val saxoToken: String,

    @Value("\${saxo-sim-api.base-url}")
    private val baseUrl: String,

    private val objectMapper: ObjectMapper
) {
    private val client = HttpClient.newHttpClient()


    //TODO implement
    fun sendOrder(
        stockAccount: TradingAccount,
        symbol: String,
        quantity: Long,
        price: BigDecimal,
        type: OrderType
    ): OrderStatus {
        return OrderStatus.FILLED
    }

    fun getSaxoClient(): SaxoClientDTO {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/port/v1/clients/me"))
            .header("Authorization", "Bearer $saxoToken")
            .GET()
            .build()

        val response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        )

        if (response.statusCode() != 200) {
            throw IllegalStateException(
                "Failed to fetch Saxo client. Status=${response.statusCode()}, Body=${response.body()}"
            )
        }

        return objectMapper.readValue<SaxoClientDTO>(response.body())
    }
}