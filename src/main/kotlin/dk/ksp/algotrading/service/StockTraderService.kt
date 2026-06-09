package dk.ksp.algotrading.service

import dk.ksp.algotrading.dto.response.StockTraderDTO
import dk.ksp.algotrading.dto.response.StockTraderWithTradingAccountsDTO
import dk.ksp.algotrading.entity.StockTrader
import dk.ksp.algotrading.mapper.toStockTraderDTOs
import dk.ksp.algotrading.mapper.toStockTraderWithTradingAccountsDTO
import dk.ksp.algotrading.repository.StockTraderRepository
import dk.ksp.algotrading.repository.StockTradingAccountRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class StockTraderService(
    private val stockTraderRepository: StockTraderRepository,
    private val stockTradingAccountRepository: StockTradingAccountRepository
) {
    fun createStockTrader(username: String): StockTraderWithTradingAccountsDTO {
        val savedStockTrader = stockTraderRepository.save(
            StockTrader.create(username)
        )
        return savedStockTrader.toStockTraderWithTradingAccountsDTO()
    }

    @Transactional
    fun deleteStockTrader(stockTraderId: Long) {
        val deletedAtTimestamp = Instant.now()
        val trader = stockTraderRepository.findActiveById(stockTraderId)
            ?: throw IllegalArgumentException("Trader not found")

        val accounts = stockTradingAccountRepository.findAllActiveByTraderIdWithHoldings(trader.id)

        accounts.forEach {
            if (it.holdings.isNotEmpty()) {
                throw IllegalArgumentException("Cannot delete trader having account with holdings")
            }

            if (it.cashBalance.signum() != 0) {
                throw IllegalArgumentException("Cannot delete trader with account cash balance")
            }

            it.deletedAt = deletedAtTimestamp
        }

        trader.deletedAt = deletedAtTimestamp
    }

    fun getStockTrader(stockTraderId: Long): StockTraderWithTradingAccountsDTO {
        val stockTrader = stockTraderRepository.findActiveByIdWithActiveTradingAccounts(stockTraderId)
            ?: throw IllegalArgumentException("Trader not found")

        return stockTrader.toStockTraderWithTradingAccountsDTO()
    }

    fun getStockTraders(): List<StockTraderDTO> {
        return stockTraderRepository.findAllByDeletedAtIsNull().toStockTraderDTOs()
    }
}