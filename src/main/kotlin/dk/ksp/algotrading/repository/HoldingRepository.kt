package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.Holding
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface HoldingRepository : JpaRepository<Holding, Long> {


//    @Query(
//        """
//    SELECT h
//    FROM Holding h
//    WHERE h.tradingAccount._id = :accountId
//    AND h.symbol = :symbol
//    AND h.tradingAccount.deletedAt IS NULL
//    AND h.tradingAccount.trader.deletedAt IS NULL
//    """
//    )
//    fun findActiveByAccountIdAndSymbol(accountId: Long, symbol: String): Holding?
//
//    @Query(
//        """
//    SELECT h
//    FROM Holding h
//    WHERE h.tradingAccount._id = :accountId
//    AND h.tradingAccount.deletedAt IS NULL
//    AND h.tradingAccount.trader.deletedAt IS NULL
//    """
//    )
//    fun findAllActiveByAccountId(accountId: Long): List<Holding>
}