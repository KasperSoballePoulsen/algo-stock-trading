package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.MarketDataClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class StockPriceService(
    private val marketDataClient: MarketDataClient
) {
    @Scheduled(fixedDelay = 5_000)
    fun fetchStockPrices() {
        val appleStockPrice = marketDataClient.fetchRealTimeQuoteData("AAPL")

    }
}