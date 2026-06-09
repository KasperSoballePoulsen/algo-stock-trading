package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.StockTraderDTO
import dk.ksp.algotrading.dto.response.StockTraderWithTradingAccountsDTO
import dk.ksp.algotrading.entity.StockTrader

fun StockTrader.toStockTraderDTO() = StockTraderDTO(id, username)

fun StockTrader.toStockTraderWithTradingAccountsDTO() =
    StockTraderWithTradingAccountsDTO(id, username, tradingAccounts.toStockTradingAccountsDTO())

fun List<StockTrader>.toStockTraderDTOs() = map { it.toStockTraderDTO() }