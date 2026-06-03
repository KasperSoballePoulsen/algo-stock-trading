package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.QuoteDataResponseDTO
import dk.ksp.algotrading.entity.StockPrice

fun QuoteDataResponseDTO.toStockPrice(symbol: String): StockPrice =
    StockPrice(
        currentPrice = currentPrice,
        change = change,
        percentChange = percentChange,
        highPrice = highPrice,
        lowPrice = lowPrice,
        openPrice = openPrice,
        previousClosePrice = previousClosePrice,
        timestamp = timestamp,
        symbol = symbol
    )

