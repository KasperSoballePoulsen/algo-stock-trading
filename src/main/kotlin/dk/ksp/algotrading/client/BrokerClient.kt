package dk.ksp.algotrading.client

import com.fasterxml.jackson.databind.ObjectMapper
import dk.ksp.algotrading.dto.response.SaxoClientDTO
import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.BuySell
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import com.fasterxml.jackson.module.kotlin.readValue
import dk.ksp.algotrading.dto.request.OrderDuration
import dk.ksp.algotrading.dto.request.SaxoOrderRequestDTO
import dk.ksp.algotrading.dto.response.SaxoAccountBalancesDTO
import dk.ksp.algotrading.dto.response.SaxoOrderErrorResponseDTO
import dk.ksp.algotrading.dto.response.SaxoOrderSuccessResponseDTO
import dk.ksp.algotrading.enum.AssetType
import dk.ksp.algotrading.enum.DurationType
import dk.ksp.algotrading.enum.OrderType
import dk.ksp.algotrading.exception.BrokerRejectedException

@Component
class BrokerClient(
    @Value("\${saxo-sim-api.api-token}")
    private val saxoToken: String,
    @Value("\${saxo-sim-api.base-url}")
    private val baseUrl: String,
    private val objectMapper: ObjectMapper,
) {
    private val client = HttpClient.newHttpClient()


    fun sendOrder(
        saxoAccountKey: String,
        amount: Long,
        buySell: BuySell,
        orderType: OrderType,
        manualOrder: Boolean,
        uic: Long,
        assetType: String,
        orderDuration: OrderDuration
    ): Long {

        val requestBody = SaxoOrderRequestDTO(
            saxoAccountKey,
            amount,
            buySell.saxoValue,
            orderType.saxoValue,
            manualOrder,
            uic,
            assetType,
            orderDuration
        )

        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/trade/v2/orders"))
            .header("Authorization", "Bearer $saxoToken")
            .header("Content-Type", "application/json")
            .POST(
                HttpRequest.BodyPublishers.ofString(
                    objectMapper.writeValueAsString(requestBody)
                )
            )
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val body = response.body()

        if (response.statusCode() !in 200..299) {
            val error = objectMapper.readValue<SaxoOrderErrorResponseDTO>(body)

            throw BrokerRejectedException(
                message = error.message ?: "Saxo rejected order",
                errorCode = error.errorCode,
                modelState = error.modelState
            )
        }

        val success = objectMapper.readValue<SaxoOrderSuccessResponseDTO>(body)

        return success.orderId

    }

    fun getSaxoClient(): SaxoClientDTO {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/port/v1/clients/me"))
            .header("Authorization", "Bearer $saxoToken")
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw IllegalStateException(
                "Failed to fetch Saxo client. Status=${response.statusCode()}, Body=${response.body()}"
            )
        }

        return objectMapper.readValue<SaxoClientDTO>(response.body())
    }

    fun getSaxoAccountBalances(saxoClientKey: String, saxoAccountKey: String): SaxoAccountBalancesDTO {

        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/port/v1/balances?AccountKey=${saxoAccountKey}&ClientKey=${saxoClientKey}"))
            .header("Authorization", "Bearer $saxoToken")
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw IllegalStateException(
                "Failed to fetch Saxo account balances. Status=${response.statusCode()}, Body=${response.body()}"
            )
        }

        return objectMapper.readValue<SaxoAccountBalancesDTO>(response.body())
    }

}