package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.SaxoOAuthToken
import org.springframework.data.jpa.repository.JpaRepository

interface SaxoOAuthTokenRepository : JpaRepository<SaxoOAuthToken, Long>