package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoClient
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SaxoSynchronizationService(
    private val tradingAccountRepository: TradingAccountRepository,
    private val tradingAccountService: TradingAccountService,
    private val saxoClient: SaxoClient,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelayString = "PT1H", initialDelayString = "PT1H")
    fun synchronize() {
        val tradingAccount = tradingAccountRepository.getTradingAccount()

        val orderHistoryResponse = saxoClient.getOrderHistory(tradingAccount.orderHistoryNextPollUrl)

        val netPositionsResponse = saxoClient.getNetPositions(
            tradingAccount.trader.saxoClientKey,
            tradingAccount.saxoAccountKey
        )

        tradingAccountService.applySaxoSynchronization(orderHistoryResponse, netPositionsResponse.data)

        logger.info("Completed Saxo synchronization")
    }
}