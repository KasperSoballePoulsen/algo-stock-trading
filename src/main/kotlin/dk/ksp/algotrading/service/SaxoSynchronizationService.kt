package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoApiClient
import dk.ksp.algotrading.repository.SaxoTradingAccountRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SaxoSynchronizationService(
    private val saxoTradingAccountRepository: SaxoTradingAccountRepository,
    private val tradingAccountService: TradingAccountService,
    private val saxoApiClient: SaxoApiClient,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelayString = "PT1H", initialDelayString = "PT1H")
    fun synchronize() {
        val tradingAccount = saxoTradingAccountRepository.getTradingAccount()

        val orderHistoryResponse = saxoApiClient.getOrderHistory(tradingAccount.orderHistoryNextPollUrl)

        val netPositionsResponse = saxoApiClient.getNetPositions(
            tradingAccount.saxoClient.clientKey,
            tradingAccount.saxoAccountKey
        )

        tradingAccountService.applySaxoSynchronization(orderHistoryResponse, netPositionsResponse.data)

        logger.info("Completed Saxo synchronization")
    }
}