package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.StockTrader
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface StockTraderRepository : JpaRepository<StockTrader, Long> {

    @Query(
        """
    SELECT st
    FROM StockTrader st
    WHERE st._id = :stockTraderId
    AND st.deletedAt IS NULL
    """
    )
    fun findActiveById(stockTraderId: Long): StockTrader?

    @Query(
        """
    SELECT st
    FROM StockTrader st
    JOIN FETCH st.tradingAccounts ta
    WHERE st.deletedAt IS NULL
    """
    )
    fun findAllActiveWithTradingAccounts(): List<StockTrader>

    @Query(
        """
    SELECT st
    FROM StockTrader st
    JOIN FETCH st.tradingAccounts ta
    WHERE st._id = :stockTraderId
    AND st.deletedAt IS NULL
    AND ta.deletedAt IS NULL
    """
    )
    fun findActiveByIdWithActiveTradingAccounts(stockTraderId: Long): StockTrader?

    fun findAllByDeletedAtIsNull(): List<StockTrader>
}