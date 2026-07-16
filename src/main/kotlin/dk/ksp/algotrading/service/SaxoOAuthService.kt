package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoOAuthClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder

@Service
class SaxoOAuthService(
    @Value("\${saxo-sim-api.oauth.app-key}")
    private val appKey: String,

    @Value("\${saxo-sim-api.oauth.redirect-url}")
    private val redirectUrl: String,

    @Value("\${saxo-sim-api.oauth.authorization-url}")
    private val authorizationUrl: String,

    private val saxoOAuthClient: SaxoOAuthClient,
    private val saxoTokenService: SaxoTokenService
) {

    fun createAuthorizationUrl(state: String) =
        UriComponentsBuilder
            .fromUriString(authorizationUrl)
            .queryParam("response_type", "code")
            .queryParam("client_id", appKey)
            .queryParam("redirect_uri", redirectUrl)
            .queryParam("state", state)
            .build()
            .encode()
            .toUriString()


    fun handleCallback(authorizationCode: String) {
        val tokens = saxoOAuthClient.exchangeAuthorizationCode(authorizationCode)

        saxoTokenService.saveInitialTokens(tokens)
    }
}