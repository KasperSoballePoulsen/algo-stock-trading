package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.Trader
import org.springframework.data.jpa.repository.JpaRepository

interface TraderRepository : JpaRepository<Trader, Long> {
    fun findByUsername(username: String) : Trader?
}