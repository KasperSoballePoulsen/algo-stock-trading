package dk.ksp.algotrading.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "saxo_oauth_token")
class SaxoOAuthToken(

    @Id
    val id: Long = 1L,

    @Column(nullable = false)
    var accessToken: String,

    @Column(nullable = false)
    var refreshToken: String,

    @Column(nullable = false)
    var tokenType: String,

    @Column(nullable = false)
    var accessTokenExpiresAt: Instant,

    @Column(nullable = false)
    var refreshTokenExpiresAt: Instant
)