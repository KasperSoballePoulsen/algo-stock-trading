package dk.ksp.algotrading.service

import dk.ksp.algotrading.dto.response.StockTraderWithTradingAccountDTO
import dk.ksp.algotrading.entity.StockTrader
import dk.ksp.algotrading.entity.StockTradingAccount
import dk.ksp.algotrading.mapper.toStockTraderWithTradingAccountDTO
import dk.ksp.algotrading.repository.StockTraderRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant

@Service
class StockTraderService(
    private val stockTraderRepository: StockTraderRepository
) {
    fun createStockTrader(username: String): StockTraderWithTradingAccountDTO {
        val savedStockTrader = stockTraderRepository.save(
            StockTrader(username, StockTradingAccount(BigDecimal.ZERO))
        )
        return savedStockTrader.toStockTraderWithTradingAccountDTO()
    }

    @Transactional
    fun deleteTrader(stockTraderId: Long) {
        val trader = stockTraderRepository.findByIdAndDeletedAtIsNullWithTradingAccountWithHoldings(stockTraderId)
            ?: throw IllegalArgumentException("Trader not found")

        if (trader.tradingAccount.holdings.isNotEmpty()) {
            throw IllegalStateException("Cannot delete trader with holdings")
        }

        trader.deletedAt = Instant.now()
    }

    fun getStockTrader(stockTraderId: Long): StockTraderWithTradingAccountDTO {
        val stockTrader = stockTraderRepository.findByIdAndDeletedAtIsNullWithTradingAccount(stockTraderId)
            ?: throw IllegalArgumentException("Trader not found")
        return stockTrader.toStockTraderWithTradingAccountDTO()
    }
}