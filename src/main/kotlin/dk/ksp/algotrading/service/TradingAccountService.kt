package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoClient
import dk.ksp.algotrading.dto.response.PortfolioDTO
import dk.ksp.algotrading.mapper.toPortfolioDTO
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class TradingAccountService(
    private val tradingAccountRepository: TradingAccountRepository,
    private val saxoClient: SaxoClient,
    private val holdingService: HoldingService,
    private val tradingService: TradingService
) {
    fun getPortfolio(syncWithSaxo: Boolean = false): PortfolioDTO {
        val account = tradingAccountRepository.getTradingAccount()

        val holdings = if (syncWithSaxo) {
            val saxoNetPositions = saxoClient.getNetPositions(
                account.trader.saxoClientKey, account.saxoAccountKey
            ).data

            holdingService.replaceHoldings(account, saxoNetPositions)
        } else {
            holdingService.getHoldings(account.id)
        }

        return account.toPortfolioDTO(holdings)
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    fun synchronizeOrderHistory() {
        val tradingAccount = tradingAccountRepository.getTradingAccount()

        val orderHistory = saxoClient.getOrderHistory(
            tradingAccount.trader.saxoClientKey,
            tradingAccount.saxoAccountKey
        )

        tradingService.reconcileOrderHistory(orderHistory)
    }
}