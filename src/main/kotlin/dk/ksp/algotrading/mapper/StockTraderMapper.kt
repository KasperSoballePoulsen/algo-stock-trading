package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.StockTraderDTO
import dk.ksp.algotrading.dto.response.StockTraderWithTradingAccountWithHoldingsDTO
import dk.ksp.algotrading.entity.StockTrader

fun StockTrader.toStockTraderDTO() =
    StockTraderDTO(id ?: throw IllegalStateException("StockTrader ID is null"), username)

fun StockTrader.toStockTraderWithTradingAccountDTO() =
    StockTraderWithTradingAccountWithHoldingsDTO(
        id ?: throw IllegalStateException("StockTrader ID is null"),
        username,
        tradingAccount.toStockTradingAccountDTO()
    )

fun List<StockTrader>.toStockTraderDTOs() = map { it.toStockTraderDTO() }