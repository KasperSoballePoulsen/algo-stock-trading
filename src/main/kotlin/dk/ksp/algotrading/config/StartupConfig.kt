package dk.ksp.algotrading.config

import dk.ksp.algotrading.client.SaxoClient
import dk.ksp.algotrading.entity.TradingAccount
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StartupConfig(
    private val saxoClient: SaxoClient,
    private val tradingAccountRepository: TradingAccountRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun initTrader() = CommandLineRunner {
        if (tradingAccountRepository.count() == 0L) {
            logger.info("Initializing trader in database")
            val saxoClient = saxoClient.getSaxoClient()

            tradingAccountRepository.save(
                TradingAccount.createWithTrader(
                    "Kasper Søballe Poulsen",
                    saxoClient.clientKey,
                    saxoClient.defaultAccountKey,
                )
            )
        }
    }
}