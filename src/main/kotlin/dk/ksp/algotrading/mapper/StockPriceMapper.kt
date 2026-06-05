package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.QuoteDataDTO
import dk.ksp.algotrading.entity.StockPrice

fun QuoteDataDTO.toStockPrice(symbol: String) =
    StockPrice(
        currentPrice,
        change,
        percentChange,
        highPrice,
        lowPrice,
        openPrice,
        previousClosePrice,
        symbol,
        timestamp
    )

