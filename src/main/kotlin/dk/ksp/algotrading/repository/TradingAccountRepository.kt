package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.TradingAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TradingAccountRepository : JpaRepository<TradingAccount, Long> {

    @Query(
        """
    SELECT ta
    FROM TradingAccount ta
    LEFT JOIN FETCH ta.holdings h
    WHERE ta.trader._id = :traderId
    AND ta.deletedAt IS NULL
    AND ta.trader.deletedAt IS NULL
    """
    )
    fun findAllActiveByTraderIdWithHoldings(traderId: Long): List<TradingAccount>

    @Query(
        """
    SELECT ta
    FROM TradingAccount ta
    WHERE ta._id = :accountId
    AND ta.deletedAt IS NULL
    AND ta.trader.deletedAt IS NULL
    """
    )
    fun findActiveById(accountId: Long): TradingAccount?

    @Query(
        """
    SELECT ta
    FROM TradingAccount ta
    LEFT JOIN FETCH ta.holdings
    WHERE ta._id = :accountId
    AND ta.deletedAt IS NULL
    AND ta.trader.deletedAt IS NULL
    """
    )
    fun findActiveByIdWithHoldings(accountId: Long): TradingAccount?
}