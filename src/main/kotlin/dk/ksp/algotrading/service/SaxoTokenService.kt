package dk.ksp.algotrading.service

import dk.ksp.algotrading.dto.saxo.response.SaxoTokenResponseDTO
import dk.ksp.algotrading.entity.SaxoOAuthToken
import dk.ksp.algotrading.event.SaxoAccessTokenRefreshedEvent
import dk.ksp.algotrading.client.SaxoOAuthClient
import dk.ksp.algotrading.repository.SaxoOAuthTokenRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class SaxoTokenService(
    private val tokenRepository: SaxoOAuthTokenRepository,
    private val saxoOAuthClient: SaxoOAuthClient,
    private val eventPublisher: ApplicationEventPublisher
) {
    private val refreshLock = Any()

    fun hasToken(): Boolean =
        tokenRepository.existsById(TOKEN_ID)

    @Transactional
    fun saveInitialTokens(
        response: SaxoTokenResponseDTO
    ) {
        saveTokens(response)
    }

    fun getValidAccessToken(): String {
        var refreshedAccessToken: String? = null

        val accessToken = synchronized(refreshLock) {
            val token = tokenRepository.findById(TOKEN_ID)
                .orElseThrow {
                    IllegalStateException(
                        "Saxo has not been authorized. " +
                                "Open /api/saxo/oauth/login first."
                    )
                }

            val refreshThreshold =
                Instant.now().plusSeconds(REFRESH_MARGIN_SECONDS)

            if (token.accessTokenExpiresAt.isAfter(refreshThreshold)) {
                return@synchronized token.accessToken
            }

            if (token.refreshTokenExpiresAt.isBefore(Instant.now())) {
                throw IllegalStateException(
                    "Saxo refresh token has expired. " +
                            "Authorization is required again."
                )
            }

            val response = saxoOAuthClient.refreshTokens(
                token.refreshToken
            )

            val updatedToken = createToken(response)

            tokenRepository.saveAndFlush(updatedToken)

            refreshedAccessToken = updatedToken.accessToken

            updatedToken.accessToken
        }

        refreshedAccessToken?.let {
            eventPublisher.publishEvent(
                SaxoAccessTokenRefreshedEvent(it)
            )
        }

        return accessToken
    }

    private fun saveTokens(
        response: SaxoTokenResponseDTO
    ) {
        tokenRepository.save(createToken(response))
    }

    private fun createToken(
        response: SaxoTokenResponseDTO
    ): SaxoOAuthToken {
        val now = Instant.now()

        return SaxoOAuthToken(
            id = TOKEN_ID,
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            tokenType = response.tokenType,
            accessTokenExpiresAt =
                now.plusSeconds(response.expiresIn),
            refreshTokenExpiresAt =
                now.plusSeconds(response.refreshTokenExpiresIn)
        )
    }

    companion object {
        private const val TOKEN_ID = 1L
        private const val REFRESH_MARGIN_SECONDS = 120L
    }
}