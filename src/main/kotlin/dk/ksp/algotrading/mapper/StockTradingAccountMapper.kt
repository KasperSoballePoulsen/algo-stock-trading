package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.StockTradingAccountDTO
import dk.ksp.algotrading.dto.response.StockTradingAccountWithHoldingsDTO
import dk.ksp.algotrading.entity.StockTradingAccount

fun StockTradingAccount.toStockTradingAccountDTO() = StockTradingAccountDTO(id , cashBalance)

fun List<StockTradingAccount>.toStockTradingAccountsDTO() = map { it.toStockTradingAccountDTO() }

fun StockTradingAccount.toStockTradingAccountWithHoldingsDTO() = StockTradingAccountWithHoldingsDTO(id, cashBalance, holdings.toStockHoldingsDTO())