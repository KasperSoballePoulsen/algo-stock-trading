package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.MarketDataClient
import dk.ksp.algotrading.client.NotificationClient
import dk.ksp.algotrading.dto.response.StockTradingAccountDTO
import dk.ksp.algotrading.entity.StockHolding
import dk.ksp.algotrading.entity.StockOrder
import dk.ksp.algotrading.entity.StockTrader
import dk.ksp.algotrading.entity.StockTradingAccount
import dk.ksp.algotrading.enum.OrderType
import dk.ksp.algotrading.mapper.toStockPrice
import dk.ksp.algotrading.repository.StockHoldingRepository
import dk.ksp.algotrading.repository.StockOrderRepository
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
    private val stockHoldingRepository: StockHoldingRepository,
    private val stockOrderRepository: StockOrderRepository,
    private val notificationClient: NotificationClient,
    private val marketDataClient: MarketDataClient,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun completeOrderInDatabase(
        tradingAccount: StockTradingAccount,
        symbol: String,
        quantity: Long,
        price: BigDecimal,
        type: OrderType
    ) {
        stockOrderRepository.save(StockOrder(symbol, type, quantity, price, tradingAccount))
        updatePortfolio(tradingAccount, symbol, quantity, type)
    }


    private fun updatePortfolio(
        tradingAccount: StockTradingAccount,
        symbol: String,
        quantity: Long,
        type: OrderType
    ) {

        val normalizedSymbol = symbol.uppercase()

        val existingHolding = stockHoldingRepository.findByTradingAccountAndSymbol(tradingAccount, normalizedSymbol)

        when (type) {
            OrderType.BUY -> {
                if (existingHolding != null)
                    existingHolding.quantity += quantity
                else
                    stockHoldingRepository.save(StockHolding(normalizedSymbol, quantity, tradingAccount))
            }

            OrderType.SELL -> {
                if (existingHolding == null)
                    throw IllegalArgumentException("Cannot sell shares not owned")

                if (existingHolding.quantity < quantity)
                    throw IllegalArgumentException("Cannot sell more shares than owned")

                existingHolding.quantity -= quantity

                if (existingHolding.quantity == 0L)
                    stockHoldingRepository.delete(existingHolding)
            }
        }
    }

    @Scheduled(cron = "0 0 22 * * MON-FRI", zone = "Europe/Copenhagen")
    fun calculatePortfolioDailyPercentChange() {
        val stockTraders = stockTraderRepository.findAllByDeletedAtIsNullWithTradingAccount()

        val portfolioChanges = stockTraders.map { trader ->
            try {
                val stockHoldings = stockHoldingRepository.findByTradingAccount(trader.tradingAccount)

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