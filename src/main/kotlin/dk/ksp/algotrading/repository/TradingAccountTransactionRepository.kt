package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.TradingAccountTransaction
import org.springframework.data.jpa.repository.JpaRepository

interface TradingAccountTransactionRepository : JpaRepository<TradingAccountTransaction, Long>

