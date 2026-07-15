package dk.ksp.algotrading.config

import dk.ksp.algotrading.client.SaxoClient
import dk.ksp.algotrading.entity.TradingAccount
import dk.ksp.algotrading.repository.TradingAccountRepository
import dk.ksp.algotrading.service.SaxoSynchronizationService
import dk.ksp.algotrading.service.SaxoTokenService
import dk.ksp.algotrading.service.StreamingService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import java.time.Instant

@Configuration
class StartupConfig(
    private val saxoClient: SaxoClient,
    private val tradingAccountRepository: TradingAccountRepository,
    private val tradeMessageStreamingService: StreamingService,
    private val saxoSynchronizationService: SaxoSynchronizationService,
    @Value("\${saxo-sim-api.base-url}")
    private val baseUrl: String,
    private val saxoTokenService: SaxoTokenService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun initTrader() = CommandLineRunner {
        if (!saxoTokenService.hasToken()) {
            logger.warn("Saxo has not been authorized. Open /api/saxo/oauth/login")
            return@CommandLineRunner
        }

        if (tradingAccountRepository.count() == 0L) {
            logger.info("Initializing trader in database")
            val saxoClientDetails = saxoClient.getSaxoClient()

            val initialOrderHistoryUrl =
                "$baseUrl/cs/v1/audit/orderactivities" +
                        "?\$top=200" +
                        "&EntryType=Last" +
                        "&ClientKey=${saxoClientDetails.clientKey}" +
                        "&AccountKey=${saxoClientDetails.defaultAccountKey}" +
                        "&FromDateTime=${Instant.now()}" // later FromDateTime should be now - 24h

            tradingAccountRepository.save(
                TradingAccount.createWithTrader(
                    "Kasper",
                    saxoClientDetails.clientKey,
                    saxoClientDetails.defaultAccountKey,
                    saxoClientDetails.defaultAccountId,
                    initialOrderHistoryUrl
                )
            )
        }
    }

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady() {
        if (!saxoTokenService.hasToken()) {
            return
        }

        saxoSynchronizationService.synchronize()
        tradeMessageStreamingService.connect()
    }
}