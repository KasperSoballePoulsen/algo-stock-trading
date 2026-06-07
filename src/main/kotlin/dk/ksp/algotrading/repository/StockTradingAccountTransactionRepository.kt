package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.StockTradingAccountTransaction
import org.springframework.data.jpa.repository.JpaRepository

interface StockTradingAccountTransactionRepository : JpaRepository<StockTradingAccountTransaction, Long>

