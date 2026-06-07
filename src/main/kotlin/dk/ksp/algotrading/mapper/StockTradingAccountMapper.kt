package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.StockTradingAccountWithHoldingsDTO
import dk.ksp.algotrading.entity.StockTradingAccount

fun StockTradingAccount.toStockTradingAccountDTO() =
    StockTradingAccountWithHoldingsDTO(cashBalance, holdings.toStockHoldingsDTO())