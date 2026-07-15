package dk.ksp.algotrading.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dk.ksp.algotrading.dto.saxo.response.SaxoTokenResponseDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.Base64

@Component
class SaxoOAuthClient(
    @Value("\${saxo-sim-api.oauth.app-key}")
    private val appKey: String,

    @Value("\${saxo-sim-api.oauth.app-secret}")
    private val appSecret: String,

    @Value("\${saxo-sim-api.oauth.redirect-url}")
    private val redirectUrl: String,

    @Value("\${saxo-sim-api.oauth.token-url}")
    private val tokenUrl: String,

    private val objectMapper: ObjectMapper,
    private val httpClient: HttpClient
) {

    fun exchangeAuthorizationCode(authorizationCode: String): SaxoTokenResponseDTO {
        return requestTokens(
            mapOf(
                "grant_type" to "authorization_code",
                "code" to authorizationCode,
                "redirect_uri" to redirectUrl
            )
        )
    }

    fun refreshTokens(
        refreshToken: String
    ): SaxoTokenResponseDTO {
        return requestTokens(
            mapOf(
                "grant_type" to "refresh_token",
                "refresh_token" to refreshToken,
                "redirect_uri" to redirectUrl
            )
        )
    }

    private fun requestTokens(
        parameters: Map<String, String>
    ): SaxoTokenResponseDTO {
        val credentials = Base64.getEncoder().encodeToString(
            "$appKey:$appSecret"
                .toByteArray(StandardCharsets.UTF_8)
        )

        val requestBody = parameters.entries.joinToString("&") {
            "${encode(it.key)}=${encode(it.value)}"
        }

        val request = HttpRequest.newBuilder()
            .uri(URI.create(tokenUrl))
            .header(
                "Content-Type",
                "application/x-www-form-urlencoded"
            )
            .header(
                "Authorization",
                "Basic $credentials"
            )
            .POST(
                HttpRequest.BodyPublishers.ofString(requestBody)
            )
            .build()

        val response = httpClient.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        )

        if (response.statusCode() !in 200..299) {
            throw IllegalStateException(
                "Saxo OAuth token request failed. " +
                        "Status=${response.statusCode()}, " +
                        "body=${response.body()}"
            )
        }

        return objectMapper.readValue(response.body())
    }

    private fun encode(value: String): String =
        URLEncoder.encode(
            value,
            StandardCharsets.UTF_8
        )
}