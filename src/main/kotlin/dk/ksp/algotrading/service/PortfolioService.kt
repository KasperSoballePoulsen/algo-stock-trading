package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.MarketDataClient
import dk.ksp.algotrading.client.NotificationClient
import dk.ksp.algotrading.dto.response.StockTraderWithPortfolioDTO
import dk.ksp.algotrading.entity.StockHolding
import dk.ksp.algotrading.mapper.toStockPrice
import dk.ksp.algotrading.mapper.toStockTraderWithPortfolioDTO
import dk.ksp.algotrading.repository.StockHoldingRepository
import dk.ksp.algotrading.repository.StockTraderRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class PortfolioService(
    private val stockTraderRepository: StockTraderRepository,
    private val notificationClient: NotificationClient,
    private val stockHoldingRepository: StockHoldingRepository,
    private val marketDataClient: MarketDataClient
) {
    private val logger = LoggerFactory.getLogger(javaClass)


    fun createOrder() {

    }



    private fun addStockHolding(username: String, symbol: String, quantity: Long): StockTraderWithPortfolioDTO {

        val stockTrader = stockTraderRepository.findByUsernameWithPortfolio(username)
            ?: throw IllegalArgumentException("Trader not found")

        val normalizedSymbol = symbol.uppercase()

        val stockHolding =
            stockTrader.portfolio
                .find { it.symbol == normalizedSymbol }
                ?.apply { this.quantity += quantity }
                ?: StockHolding(stockTrader, normalizedSymbol, quantity)
                    .also { stockTrader.portfolio.add(it) }

        val stockText = if (stockHolding.quantity == 1L) "stock" else "stocks"

        notificationClient.sendNotification(
            "$username now has ${stockHolding.quantity} ${stockHolding.symbol} $stockText",
            "Portfolio Update"
        )
        return stockTrader.toStockTraderWithPortfolioDTO()
    }

    private fun removeStockHolding(username: String, symbol: String, quantity: Long): StockTraderWithPortfolioDTO {

        val stockTrader = stockTraderRepository.findByUsernameWithPortfolio(username)
            ?: throw IllegalArgumentException("Trader not found")

        val normalizedSymbol = symbol.uppercase()

        val stockHolding =
            stockTrader.portfolio
                .find { it.symbol == normalizedSymbol }
                ?.apply {
                    if (quantity > this.quantity)
                        throw IllegalArgumentException("Cannot remove more shares than owned")
                    this.quantity -= quantity
                }?.also {
                    if (it.quantity == 0L)
                        stockTrader.portfolio.remove(it)
                }
                ?: throw IllegalArgumentException("$normalizedSymbol not found in portfolio")

        val stockText = if (stockHolding.quantity == 1L) "stock" else "stocks"

        notificationClient.sendNotification(
            "$username now has ${stockHolding.quantity} ${stockHolding.symbol} $stockText",
            "Portfolio Update"
        )

        return stockTrader.toStockTraderWithPortfolioDTO()
    }

    //    @Scheduled(initialDelay = 5_000)
    @Scheduled(cron = "0 0 22 * * MON-FRI", zone = "Europe/Copenhagen")
    fun calculatePortfolioDailyPercentChange() {
        val stockTraders = stockTraderRepository.findAll()

        val portfolioChanges = stockTraders.map { trader ->
            try {
                val stockHoldings = stockHoldingRepository.findByTrader(trader)

                if (stockHoldings.isEmpty()) {
                    return@map "${trader.username}: No holdings"
                }

                val pricesBySymbol = stockHoldings
                    .map { marketDataClient.fetchRealTimeQuoteData(it.symbol).toStockPrice(it.symbol) }
                    .associateBy { it.symbol }

                val values = stockHoldings.map {
                    val price = pricesBySymbol[it.symbol]
                        ?: throw IllegalStateException("Missing price for ${it.symbol}")

                    Pair(
                        it.quantity.toBigDecimal() * price.previousClosePrice,
                        it.quantity.toBigDecimal() * price.currentPrice
                    )
                }

                val yesterdaysValue = values.sumOf { it.first }
                val todaysValue = values.sumOf { it.second }

                val percentChange =
                    todaysValue
                        .subtract(yesterdaysValue)
                        .divide(yesterdaysValue, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal("100"))
                        .setScale(2, RoundingMode.HALF_UP)

                "${trader.username}: $percentChange%"
            } catch (e: Exception) {
                logger.error("Failed to calculate portfolio for {}", trader.username, e)
                "${trader.username}: ERROR"
            }
        }

        val message = portfolioChanges.joinToString("\n")

        notificationClient.sendNotification(message, "Portfolio Daily Change")
    }

}