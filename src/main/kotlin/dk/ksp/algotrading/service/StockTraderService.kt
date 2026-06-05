package dk.ksp.algotrading.service

import dk.ksp.algotrading.dto.response.StockTraderDTO
import dk.ksp.algotrading.dto.response.StockTraderWithPortfolioDTO
import dk.ksp.algotrading.entity.StockTrader
import dk.ksp.algotrading.mapper.toStockTraderDTO
import dk.ksp.algotrading.mapper.toStockTraderWithPortfolioDTO
import dk.ksp.algotrading.repository.StockTraderRepository
import org.springframework.stereotype.Service

@Service
class StockTraderService(
    private val stockTraderRepository: StockTraderRepository
) {
    fun createStockTrader(username: String): StockTraderDTO {
        val stockTrader = StockTrader(username)
        stockTraderRepository.save(stockTrader)
        return stockTrader.toStockTraderDTO()
    }

    fun removeStockTrader(username: String): StockTraderDTO {
        val stockTrader = stockTraderRepository.findByUsername(username)
            ?: throw IllegalArgumentException("Trader not found")

        stockTraderRepository.delete(stockTrader)
        return stockTrader.toStockTraderDTO()
    }

    fun getStockTrader(username: String): StockTraderWithPortfolioDTO {
        val stockTrader = stockTraderRepository.findByUsernameWithPortfolio(username)
            ?: throw IllegalArgumentException("Trader not found")
        return stockTrader.toStockTraderWithPortfolioDTO()
    }
}