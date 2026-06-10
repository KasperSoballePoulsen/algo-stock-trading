package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.StockTradingAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface StockTradingAccountRepository : JpaRepository<StockTradingAccount, Long> {

    @Query(
        """
    SELECT sta
    FROM StockTradingAccount sta
    LEFT JOIN FETCH sta.holdings h
    WHERE sta.stockTrader._id = :traderId
    AND sta.deletedAt IS NULL
    AND sta.stockTrader.deletedAt IS NULL
    """
    )
    fun findAllActiveByTraderIdWithHoldings(traderId: Long): List<StockTradingAccount>

    @Query(
        """
    SELECT sta
    FROM StockTradingAccount sta
    WHERE sta._id = :accountId
    AND sta.deletedAt IS NULL
    AND sta.stockTrader.deletedAt IS NULL
    """
    )
    fun findActiveById(accountId: Long): StockTradingAccount?

    @Query(
        """
    SELECT sta
    FROM StockTradingAccount sta
    LEFT JOIN FETCH sta.holdings
    WHERE sta._id = :accountId
    AND sta.deletedAt IS NULL
    AND sta.stockTrader.deletedAt IS NULL
    """
    )
    fun findActiveByIdWithHoldings(accountId: Long): StockTradingAccount?
}