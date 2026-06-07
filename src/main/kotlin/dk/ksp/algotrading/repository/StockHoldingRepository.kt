package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.StockHolding
import dk.ksp.algotrading.entity.StockTradingAccount
import org.springframework.data.jpa.repository.JpaRepository

interface StockHoldingRepository : JpaRepository<StockHolding, Long> {

    fun findByTradingAccount(tradingAccount: StockTradingAccount): List<StockHolding>

    fun findByTradingAccountAndSymbol(tradingAccount: StockTradingAccount, symbol: String): StockHolding?
}