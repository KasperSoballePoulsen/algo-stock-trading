package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.SaxoTradingAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SaxoTradingAccountRepository : JpaRepository<SaxoTradingAccount, Long> {

    @Query("""
    SELECT sta
    FROM SaxoTradingAccount sta
    """)
    fun getTradingAccount(): SaxoTradingAccount

    @Query("""
    SELECT sta
    FROM SaxoTradingAccount sta
    WHERE sta.saxoClient.user.username = :username
    """)
    fun findByUsername(username: String): SaxoTradingAccount?

    fun findBySaxoAccountId(saxoAccountId: String): SaxoTradingAccount?

}