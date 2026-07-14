package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoClient
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SaxoSynchronizationService(
    private val tradingAccountRepository: TradingAccountRepository,
    private val tradingAccountService: TradingAccountService,
    private val saxoClient: SaxoClient,
) {

    @Scheduled(fixedDelayString = "PT3M", initialDelayString = "PT3M")
    fun synchronize() {
        val tradingAccount = tradingAccountRepository.getTradingAccount()

        val orderHistoryResponse = saxoClient.getOrderHistory(tradingAccount.orderHistoryNextPollUrl)

        val netPositionsResponse = saxoClient.getNetPositions(
            tradingAccount.trader.saxoClientKey,
            tradingAccount.saxoAccountKey
        )

        tradingAccountService.applySaxoSynchronization(orderHistoryResponse, netPositionsResponse.data)
    }
}