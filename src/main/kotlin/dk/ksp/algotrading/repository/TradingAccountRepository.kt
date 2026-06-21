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
    """
    )
    fun getTradingAccount(): TradingAccount

}