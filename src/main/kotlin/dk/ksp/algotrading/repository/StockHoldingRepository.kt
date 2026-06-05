package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.StockHolding
import dk.ksp.algotrading.entity.StockTrader
import org.springframework.data.jpa.repository.JpaRepository

interface StockHoldingRepository : JpaRepository<StockHolding, Long> {

    fun findByTrader(trader: StockTrader): List<StockHolding>
}