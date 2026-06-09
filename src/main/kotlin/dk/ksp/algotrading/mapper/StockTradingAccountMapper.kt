package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.StockTradingAccountDTO
import dk.ksp.algotrading.entity.StockTradingAccount

fun StockTradingAccount.toStockTradingAccountDTO() = StockTradingAccountDTO(id , cashBalance)

fun List<StockTradingAccount>.toStockTradingAccountsDTO() = map { it.toStockTradingAccountDTO() }