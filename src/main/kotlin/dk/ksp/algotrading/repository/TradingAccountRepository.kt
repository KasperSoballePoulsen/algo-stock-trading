package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.TradingAccount
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
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
    JOIN FETCH ta.trader t
    WHERE ta._id = :accountId
    AND ta.deletedAt IS NULL
    AND t.deletedAt IS NULL
    """
    )
    fun findActiveByIdWithTrader(accountId: Long): TradingAccount?

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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
        """
    SELECT ta
    FROM TradingAccount ta
    WHERE ta.id = :accountId
    AND ta.deletedAt IS NULL
    AND ta.trader.deletedAt IS NULL
    """
    )
    fun findActiveByIdForUpdate(accountId: Long): TradingAccount?
}