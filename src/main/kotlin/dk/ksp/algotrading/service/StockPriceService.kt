package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.MarketDataClient
import dk.ksp.algotrading.client.NotificationClient
import dk.ksp.algotrading.entity.StockPrice
import dk.ksp.algotrading.mapper.toStockPrice
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class StockPriceService(
    private val marketDataClient: MarketDataClient,
    private val notificationClient: NotificationClient
) {

    fun fetchStockPrices() {
        val astsStockPrice = marketDataClient.fetchRealTimeQuoteData("ASTS").toStockPrice("ASTS")


        notificationClient.sendNotification("${astsStockPrice.symbol}: ${astsStockPrice.percentChange}%", "stocks")
    }
}