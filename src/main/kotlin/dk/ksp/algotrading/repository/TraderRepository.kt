package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.Trader
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TraderRepository : JpaRepository<Trader, Long> {

    @Query(
        """
    SELECT t
    FROM Trader t
    WHERE t._id = :traderId
    AND t.deletedAt IS NULL
    """
    )
    fun findActiveById(traderId: Long): Trader?

    @Query(
        """
    SELECT t
    FROM Trader t
    JOIN FETCH t.tradingAccounts ta
    WHERE t.deletedAt IS NULL
    """
    )
    fun findAllActiveWithTradingAccounts(): List<Trader>

    @Query(
        """
    SELECT t
    FROM Trader t
    JOIN FETCH t.tradingAccounts ta
    WHERE t._id = :traderId
    AND t.deletedAt IS NULL
    AND ta.deletedAt IS NULL
    """
    )
    fun findActiveByIdWithActiveTradingAccounts(traderId: Long): Trader?

    fun findAllByDeletedAtIsNull(): List<Trader>
}