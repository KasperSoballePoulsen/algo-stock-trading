package dk.ksp.algotrading.service

import dk.ksp.algotrading.dto.response.StockTraderDTO
import dk.ksp.algotrading.dto.response.StockTraderWithPortfolioDTO
import dk.ksp.algotrading.entity.StockTrader
import dk.ksp.algotrading.mapper.toStockTraderDTO
import dk.ksp.algotrading.mapper.toStockTraderWithPortfolioDTO
import dk.ksp.algotrading.repository.StockTraderRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime

@Service
class StockTraderService(
    private val stockTraderRepository: StockTraderRepository
) {
    fun createStockTrader(username: String): StockTraderDTO {
        val stockTrader = StockTrader(username)
        stockTraderRepository.save(stockTrader)
        return stockTrader.toStockTraderDTO()
    }

    @Transactional
    fun deleteTrader(username: String) {
        val trader = stockTraderRepository.findByUsernameAndDeletedAtIsNullWithPortfolio(username)
            ?: throw IllegalArgumentException("Trader not found")

        if (trader.portfolio.isNotEmpty()) {
            throw IllegalStateException("Cannot delete trader with holdings")
        }

        trader.deletedAt = Instant.now()
    }

    fun getStockTrader(username: String): StockTraderWithPortfolioDTO {
        val stockTrader = stockTraderRepository.findByUsernameAndDeletedAtIsNullWithPortfolio(username)
            ?: throw IllegalArgumentException("Trader not found")
        return stockTrader.toStockTraderWithPortfolioDTO()
    }
}