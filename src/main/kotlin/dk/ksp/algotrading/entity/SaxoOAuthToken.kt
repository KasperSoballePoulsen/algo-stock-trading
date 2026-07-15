package dk.ksp.algotrading.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "saxo_oauth_token")
class SaxoOAuthToken(

    @Id
    val id: Long = 1L,

    var accessToken: String,

    var refreshToken: String,

    var tokenType: String,

    var accessTokenExpiresAt: Instant,

    var refreshTokenExpiresAt: Instant
)