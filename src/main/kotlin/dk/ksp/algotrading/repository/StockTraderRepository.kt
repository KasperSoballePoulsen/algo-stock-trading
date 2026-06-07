package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.StockTrader
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface StockTraderRepository : JpaRepository<StockTrader, Long> {

    @Query(
        """
    SELECT st
    FROM StockTrader st
    JOIN FETCH st.tradingAccount ta
    WHERE st.deletedAt IS NULL
    """
    )
    fun findAllByDeletedAtIsNullWithTradingAccount(): List<StockTrader>

    @Query(
        """
    SELECT st
    FROM StockTrader st
    JOIN FETCH st.tradingAccount ta
    WHERE st.id = :stockTraderId
    AND st.deletedAt IS NULL
    """
    )
    fun findByIdAndDeletedAtIsNullWithTradingAccount(stockTraderId: Long): StockTrader?

    @Query(
        """
    SELECT st
    FROM StockTrader st
    JOIN FETCH st.tradingAccount ta
    LEFT JOIN FETCH ta.holdings
    WHERE st.id = :stockTraderId
    AND st.deletedAt IS NULL
    """
    )
    fun findByIdAndDeletedAtIsNullWithTradingAccountWithHoldings(stockTraderId: Long): StockTrader?
}