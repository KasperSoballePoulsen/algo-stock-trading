package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.StockTrader
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface StockTraderRepository : JpaRepository<StockTrader, Long> {
    fun findByUsername(username: String): StockTrader?

    @Query(
        """
    SELECT st
    FROM StockTrader st
    LEFT JOIN FETCH st.portfolio
    WHERE st.username = :username
"""
    )
    fun findByUsernameWithPortfolio(username: String): StockTrader?
}