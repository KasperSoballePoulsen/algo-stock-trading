package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.NotificationClient
import dk.ksp.algotrading.dto.response.StockTraderWithPortfolioDTO
import dk.ksp.algotrading.entity.StockHolding
import dk.ksp.algotrading.mapper.toStockTraderWithPortfolioDTO
import dk.ksp.algotrading.repository.StockTraderRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PortfolioService(
    private val stockTraderRepository: StockTraderRepository,
    private val notificationClient: NotificationClient
) {
    @Transactional
    fun addStockHolding(username: String, symbol: String, quantity: Long): StockTraderWithPortfolioDTO {

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

    @Transactional
    fun removeStockHolding(username: String, symbol: String, quantity: Long): StockTraderWithPortfolioDTO {

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
}