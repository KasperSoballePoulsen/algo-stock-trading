package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.MarketDataClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class StockPriceService(
    private val marketDataClient: MarketDataClient
) {
    @Scheduled(initialDelay = 10_000)
    fun fetchPrices() {
        val result = marketDataClient.fetchInstruments()


    }
}