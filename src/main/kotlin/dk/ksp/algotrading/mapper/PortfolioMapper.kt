package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.StockHoldingDTO
import dk.ksp.algotrading.entity.StockHolding

fun List<StockHolding>.toPortfolioDTO() =
    map { it.toStockHoldingDTO() }


fun StockHolding.toStockHoldingDTO() =
    StockHoldingDTO(symbol, quantity)