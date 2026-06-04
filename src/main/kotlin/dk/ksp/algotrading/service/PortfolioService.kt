package dk.ksp.algotrading.service

import dk.ksp.algotrading.entity.StockHolding
import dk.ksp.algotrading.repository.StockHoldingRepository
import dk.ksp.algotrading.repository.StockTraderRepository
import org.springframework.stereotype.Service

@Service
class PortfolioService(
    private val stockHoldingRepository: StockHoldingRepository,
    private val stockTraderRepository: StockTraderRepository
) {
    fun addStockHolding(username: String, symbol: String, quantity: Long) {
        val trader =
            stockTraderRepository.findByUsername(username) ?: throw IllegalArgumentException("Trader not found")

        val existingStockHolding = stockHoldingRepository.findByTraderAndSymbol(trader, symbol)


        if (existingStockHolding != null) {
            existingStockHolding.quantity += quantity
            stockHoldingRepository.save(existingStockHolding)
        } else {
            val newStockHolding = StockHolding(trader, symbol, quantity)

            stockHoldingRepository.save(newStockHolding)
        }
    }
}