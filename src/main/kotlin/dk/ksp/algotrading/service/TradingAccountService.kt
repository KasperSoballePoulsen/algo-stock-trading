package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoClient
import dk.ksp.algotrading.dto.response.DashboardDTO
import dk.ksp.algotrading.dto.response.PerformanceDTO
import dk.ksp.algotrading.dto.response.PortfolioDTO
import dk.ksp.algotrading.dto.saxo.response.SaxoNetPosition
import dk.ksp.algotrading.dto.saxo.response.SaxoOrderActivitiesResponseDTO
import dk.ksp.algotrading.mapper.toPortfolioDTO
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TradingAccountService(
    private val tradingAccountRepository: TradingAccountRepository,
    private val saxoClient: SaxoClient,
    private val holdingService: HoldingService,
    private val tradingService: TradingService,
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

    @Transactional
    fun applySaxoSynchronization(
        orderHistoryResponse: SaxoOrderActivitiesResponseDTO,
        netPositions: List<SaxoNetPosition>
    ) {
        val tradingAccount = tradingAccountRepository.getTradingAccount()

        tradingService.reconcileOrderHistory(orderHistoryResponse.data)

        holdingService.replaceHoldings(tradingAccount, netPositions)

        tradingAccount.orderHistoryNextPollUrl = orderHistoryResponse.nextPoll
    }

    fun getPerformance(): PerformanceDTO {
        val account = tradingAccountRepository.getTradingAccount()

        val today = saxoClient.getPerformance(
            clientKey = account.trader.saxoClientKey,
            accountKey = account.saxoAccountKey,
            fromDate = LocalDate.now(),
            toDate = LocalDate.now(),
        )

        val total = saxoClient.getPerformance(
            clientKey = account.trader.saxoClientKey,
            accountKey = account.saxoAccountKey,
            standardPeriod = "AllTime",
        )

        return PerformanceDTO(
            today = today.performance,
            todayPercent = today.performancePercent,
            total = total.performance,
            totalPercent = total.performancePercent,
        )
    }
}