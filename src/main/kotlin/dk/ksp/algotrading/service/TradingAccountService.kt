package dk.ksp.algotrading.service

import dk.ksp.algotrading.dto.response.TradingAccountWithHoldingsDTO
import dk.ksp.algotrading.mapper.toTradingAccountWithHoldingsDTO
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.springframework.stereotype.Service

@Service
class TradingAccountService(
    private val tradingAccountRepository: TradingAccountRepository
) {
    fun getTradingAccount(tradingAccountId: Long): TradingAccountWithHoldingsDTO {
        val tradingAccount = tradingAccountRepository.findActiveByIdWithHoldings(tradingAccountId)
            ?: throw IllegalArgumentException("TradingAccount not found")

        return tradingAccount.toTradingAccountWithHoldingsDTO()
    }

}