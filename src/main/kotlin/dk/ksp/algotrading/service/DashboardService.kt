package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoClient
import dk.ksp.algotrading.dto.response.DashboardDTO
import dk.ksp.algotrading.dto.response.PerformanceDTO
import dk.ksp.algotrading.dto.response.TradingAccountDTO
import dk.ksp.algotrading.mapper.toHoldingsDTO
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.springframework.stereotype.Service

@Service
class DashboardService(
    private val saxoClient: SaxoClient,
    private val tradingAccountRepository: TradingAccountRepository,
    private val holdingService: HoldingService
) {

    fun getDashboard(): DashboardDTO {
        val tradingAccount = tradingAccountRepository.getTradingAccount()
        val saxoClientKey = tradingAccount.trader.saxoClientKey
        val saxoAccountKey = tradingAccount.saxoAccountKey
        val cashAvailableForTrading =
            saxoClient.getSaxoAccountBalances(saxoClientKey, saxoAccountKey).cashAvailableForTrading

        val tradingAccountDTO = TradingAccountDTO(tradingAccount.id, cashAvailableForTrading)

        val performanceDTO = PerformanceDTO(0.0, 0.0, 0.0, 0.0)

        val holdingsDTO = holdingService.getHoldings(tradingAccount.id).toHoldingsDTO()

        return DashboardDTO(tradingAccountDTO, performanceDTO, holdingsDTO)
    }

}