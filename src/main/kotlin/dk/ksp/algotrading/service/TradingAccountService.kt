package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.BrokerClient
import dk.ksp.algotrading.dto.response.PortfolioDTO
import dk.ksp.algotrading.mapper.toPortfolioDTO
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.springframework.stereotype.Service

@Service
class TradingAccountService(
    private val tradingAccountRepository: TradingAccountRepository,
    private val brokerClient: BrokerClient,
    private val holdingService: HoldingService
) {
    fun getPortfolio(syncWithSaxo: Boolean = false): PortfolioDTO {
        val account = tradingAccountRepository.getTradingAccount()

        val holdings = if (syncWithSaxo) {
            val saxoNetPositions = brokerClient.getNetPositions(
                account.trader.saxoClientKey, account.saxoAccountKey
            ).data

            holdingService.replaceHoldings(account, saxoNetPositions)
        } else {
            holdingService.getHoldings(account.id)
        }

        return account.toPortfolioDTO(holdings)
    }
}