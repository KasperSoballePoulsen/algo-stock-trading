package dk.ksp.algotrading.service

import dk.ksp.algotrading.dto.saxo.response.SaxoTokenResponseDTO
import dk.ksp.algotrading.event.SaxoAccessTokenRefreshedEvent
import dk.ksp.algotrading.client.SaxoOAuthClient
import dk.ksp.algotrading.mapper.toSaxoOAuthTokenEntity
import dk.ksp.algotrading.repository.SaxoOAuthTokenRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant

private const val TOKEN_ID = 1L
private const val REFRESH_MARGIN_SECONDS = 120L

@Service
class SaxoTokenService(
    private val saxoOAuthTokenRepository: SaxoOAuthTokenRepository,
    private val saxoOAuthClient: SaxoOAuthClient,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun hasToken() = saxoOAuthTokenRepository.existsById(TOKEN_ID)

    fun saveInitialTokens(response: SaxoTokenResponseDTO) {
        val token = response.toSaxoOAuthTokenEntity(Instant.now())
        saxoOAuthTokenRepository.save(token)
    }

    fun getValidAccessToken(): String {
        val result = getOrRefreshToken()

        if (result.refreshed) {
            eventPublisher.publishEvent(SaxoAccessTokenRefreshedEvent(result.accessToken))
        }

        return result.accessToken
    }

    @Synchronized
    private fun getOrRefreshToken(): TokenResult {
        val token = saxoOAuthTokenRepository.findById(TOKEN_ID)
            .orElseThrow {
                IllegalStateException(
                    "Saxo has not been authorized. Open /api/saxo/oauth/login first."
                )
            }

        val now = Instant.now()
        val refreshThreshold = now.plusSeconds(REFRESH_MARGIN_SECONDS)

        if (token.accessTokenExpiresAt.isAfter(refreshThreshold)) {
            return TokenResult(
                accessToken = token.accessToken,
                refreshed = false
            )
        }

        if (!token.refreshTokenExpiresAt.isAfter(now)) {
            throw IllegalStateException(
                "Saxo refresh token has expired. Authorization is required again."
            )
        }

        val response = saxoOAuthClient.refreshTokens(token.refreshToken)
        val newTokens = response.toSaxoOAuthTokenEntity(Instant.now())

        saxoOAuthTokenRepository.saveAndFlush(newTokens)

        return TokenResult(
            accessToken = newTokens.accessToken,
            refreshed = true
        )
    }

    @Scheduled(fixedDelayString = "PT1M", initialDelayString = "PT1M")
    fun refreshTokenIfNecessary() {
        if (!hasToken()) {
            return
        }

        getValidAccessToken()
    }

    private data class TokenResult(
        val accessToken: String,
        val refreshed: Boolean
    )
}