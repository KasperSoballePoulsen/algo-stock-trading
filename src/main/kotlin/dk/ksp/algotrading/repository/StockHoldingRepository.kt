package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.StockHolding
import dk.ksp.algotrading.entity.StockTradingAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface StockHoldingRepository : JpaRepository<StockHolding, Long> {


    @Query(
        """
    SELECT sh
    FROM StockHolding sh
    WHERE sh.tradingAccount._id = :accountId
    AND sh.symbol = :symbol
    AND sh.tradingAccount.deletedAt IS NULL
    AND sh.tradingAccount.stockTrader.deletedAt IS NULL
    """
    )
    fun findActiveByAccountIdAndSymbol(accountId: Long, symbol: String): StockHolding?

    @Query(
        """
    SELECT sh
    FROM StockHolding sh
    WHERE sh.tradingAccount._id = :accountId
    AND sh.tradingAccount.deletedAt IS NULL
    AND sh.tradingAccount.stockTrader.deletedAt IS NULL
    """
    )
    fun findAllActiveByAccountId(accountId: Long): List<StockHolding>
}