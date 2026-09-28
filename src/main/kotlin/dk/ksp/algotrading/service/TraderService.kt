package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoApiClient
import dk.ksp.algotrading.dto.response.TraderDTO
import dk.ksp.algotrading.mapper.toTraderWithTradingAccountDTO
import dk.ksp.algotrading.repository.SaxoTradingAccountRepository
import org.springframework.stereotype.Service


@Service
class TraderService(
    private val saxoTradingAccountRepository: SaxoTradingAccountRepository,
    private val saxoClient: SaxoApiClient
) {

    fun getTrader(): TraderDTO {
        val tradingAccount = saxoTradingAccountRepository.getTradingAccount()
        val saxoClientKey = tradingAccount.saxoClient.clientKey
        val saxoAccountKey = tradingAccount.saxoAccountKey
        val cashAvailableForTrading = saxoClient.getSaxoAccountBalances(saxoClientKey, saxoAccountKey).cashAvailableForTrading

        return tradingAccount.toTraderWithTradingAccountDTO(cashAvailableForTrading)
    }


}