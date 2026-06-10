package dk.ksp.algotrading.service

import dk.ksp.algotrading.dto.response.StockTradingAccountWithHoldingsDTO
import dk.ksp.algotrading.mapper.toStockTradingAccountWithHoldingsDTO
import dk.ksp.algotrading.repository.StockTradingAccountRepository
import org.springframework.stereotype.Service

@Service
class StockTradingAccountService(
    private val stockTradingAccountRepository: StockTradingAccountRepository
) {
    fun getStockTradingAccount(tradingAccountId: Long): StockTradingAccountWithHoldingsDTO {
        val tradingAccount = stockTradingAccountRepository.findActiveByIdWithHoldings(tradingAccountId)
            ?: throw IllegalArgumentException("StockTradingAccount not found")

        return tradingAccount.toStockTradingAccountWithHoldingsDTO()
    }

}