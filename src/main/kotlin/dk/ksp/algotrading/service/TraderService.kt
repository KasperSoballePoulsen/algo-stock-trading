package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.BrokerClient
import dk.ksp.algotrading.dto.response.TraderDTO
import dk.ksp.algotrading.dto.response.TraderWithTradingAccountsDTO
import dk.ksp.algotrading.entity.Trader
import dk.ksp.algotrading.mapper.toTraderDTOs
import dk.ksp.algotrading.mapper.toTraderWithTradingAccountsDTO
import dk.ksp.algotrading.repository.TraderRepository
import dk.ksp.algotrading.repository.TradingAccountRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TraderService(
    private val traderRepository: TraderRepository,
    private val tradingAccountRepository: TradingAccountRepository,
    private val brokerClient: BrokerClient
) {
    fun createTrader(username: String): TraderWithTradingAccountsDTO {
        val saxoClient = brokerClient.getSaxoClient()

        val savedTrader = traderRepository.save(
            Trader.create(
                username,
                saxoClient.clientKey,
                saxoClient.defaultAccountKey
            )
        )

        return savedTrader.toTraderWithTradingAccountsDTO()
    }

    @Transactional
    fun deleteTrader(traderId: Long) {
        val deletedAtTimestamp = Instant.now()
        val trader = traderRepository.findActiveById(traderId)
            ?: throw IllegalArgumentException("Trader not found")

        val accounts = tradingAccountRepository.findAllActiveByTraderIdWithHoldings(trader.id)

        accounts.forEach {
            if (it.holdings.isNotEmpty()) {
                throw IllegalArgumentException("Cannot delete trader having account with holdings")
            }

            if (it.cashAvailable.signum() != 0) {
                throw IllegalArgumentException("Cannot delete trader with account cash balance")
            }

            it.deletedAt = deletedAtTimestamp
        }

        trader.deletedAt = deletedAtTimestamp
    }

    fun getTrader(traderId: Long): TraderWithTradingAccountsDTO {
        val trader = traderRepository.findActiveByIdWithActiveTradingAccounts(traderId)
            ?: throw IllegalArgumentException("Trader not found")

        return trader.toTraderWithTradingAccountsDTO()
    }

    fun getTraders(): List<TraderDTO> {
        return traderRepository.findAllByDeletedAtIsNull().toTraderDTOs()
    }
}