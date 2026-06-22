package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.Holding
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface HoldingRepository : JpaRepository<Holding, Long> {

    @Query(
        """
    SELECT h
    FROM Holding h
    WHERE h.tradingAccount._id = :accountId
    """
    )
    fun findAllByAccountId(accountId: Long): List<Holding>


    @Modifying
    @Query(
        """
    DELETE FROM Holding h
    WHERE h.tradingAccount.id = :accountId
    """
    )
    fun deleteAllByTradingAccountId(accountId: Long)
}