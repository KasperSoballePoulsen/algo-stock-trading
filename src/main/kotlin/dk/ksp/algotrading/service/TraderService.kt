package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.SaxoClient
import dk.ksp.algotrading.dto.response.TraderWithTradingAccountDTO
import dk.ksp.algotrading.mapper.toTraderWithTradingAccountDTO
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.springframework.stereotype.Service


@Service
class TraderService(
    private val tradingAccountRepository: TradingAccountRepository,
    private val saxoClient: SaxoClient
) {

    fun getTrader(): TraderWithTradingAccountDTO {
        val tradingAccount = tradingAccountRepository.getTradingAccount()
        val saxoClientKey = tradingAccount.trader.saxoClientKey
        val saxoAccountKey = tradingAccount.saxoAccountKey
        val cashAvailableForTrading = saxoClient.getSaxoAccountBalances(saxoClientKey, saxoAccountKey).cashAvailableForTrading

        return tradingAccount.toTraderWithTradingAccountDTO(cashAvailableForTrading)
    }


}