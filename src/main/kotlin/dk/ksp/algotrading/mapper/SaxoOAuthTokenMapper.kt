package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.saxo.response.SaxoTokenResponseDTO
import dk.ksp.algotrading.entity.SaxoOAuthToken
import java.time.Instant

fun SaxoTokenResponseDTO.toSaxoOAuthTokenEntity(now: Instant) =
    SaxoOAuthToken(
        accessToken = accessToken,
        refreshToken = refreshToken,
        tokenType = tokenType,
        accessTokenExpiresAt = now.plusSeconds(expiresIn),
        refreshTokenExpiresAt = now.plusSeconds(refreshTokenExpiresIn)
    )