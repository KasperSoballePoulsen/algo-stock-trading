package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.StockTraderDTO
import dk.ksp.algotrading.dto.response.StockTraderWithPortfolioDTO
import dk.ksp.algotrading.entity.StockTrader

fun StockTrader.toStockTraderDTO() = StockTraderDTO(username)

fun StockTrader.toStockTraderWithPortfolioDTO() = StockTraderWithPortfolioDTO(username, portfolio.toPortfolioDTO())