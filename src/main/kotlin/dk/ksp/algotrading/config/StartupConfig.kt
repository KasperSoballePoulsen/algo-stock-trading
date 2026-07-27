package dk.ksp.algotrading.config

import dk.ksp.algotrading.client.SaxoApiClient
import dk.ksp.algotrading.entity.SaxoClient
import dk.ksp.algotrading.entity.User
import dk.ksp.algotrading.entity.SaxoTradingAccount
import dk.ksp.algotrading.repository.UserRepository
import dk.ksp.algotrading.repository.SaxoTradingAccountRepository
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
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant

@Configuration
class StartupConfig(
    private val saxoApiClient: SaxoApiClient,
    private val saxoTradingAccountRepository: SaxoTradingAccountRepository,
    private val userRepository: UserRepository,
    private val tradeMessageStreamingService: StreamingService,
    private val saxoSynchronizationService: SaxoSynchronizationService,
    @Value("\${saxo-sim-api.base-url}")
    private val baseUrl: String,
    private val saxoTokenService: SaxoTokenService,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${app.initial-user.username}")
    private val initialUsername: String,
    @Value("\${app.initial-user.password}")
    private val initialPassword: String
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun initTrader() = CommandLineRunner {
        if (userRepository.count() == 0L) {
            logger.info("Initializing user in database")

            userRepository.save(
                User(
                    username = initialUsername,
                    passwordHash = passwordEncoder.encode(initialPassword),
                )
            )
        }

        if (!saxoTokenService.hasToken()) {
            logger.warn("Saxo has not been authorized. Open /api/saxo/oauth/login")
            return@CommandLineRunner
        }

        initializeTradingAccountIfMissing()
    }

//    @Bean
//    fun initTrader() = CommandLineRunner {
//        if (!saxoTokenService.hasToken()) {
//            logger.warn("Saxo has not been authorized. Open /api/saxo/oauth/login")
//            return@CommandLineRunner
//        }
//
//        if (tradingAccountRepository.count() == 0L) {
//            logger.info("Initializing trader in database")
//            val saxoClientDetails = saxoClient.getSaxoClient()
//
//            val initialOrderHistoryUrl =
//                "$baseUrl/cs/v1/audit/orderactivities" +
//                        "?\$top=200" +
//                        "&EntryType=Last" +
//                        "&ClientKey=${saxoClientDetails.clientKey}" +
//                        "&AccountKey=${saxoClientDetails.defaultAccountKey}" +
//                        "&FromDateTime=${Instant.now()}" // later FromDateTime should be now - 24h
//
//            tradingAccountRepository.save(
//                TradingAccount.createWithTrader(
//                    username = initialUsername,
//                    passwordHash = passwordEncoder.encode(initialPassword),
//                    saxoClientKey = saxoClientDetails.clientKey,
//                    saxoAccountKey = saxoClientDetails.defaultAccountKey,
//                    saxoAccountId = saxoClientDetails.defaultAccountId,
//                    orderHistoryNextPollUrl = initialOrderHistoryUrl
//                )
//            )
//        }
//    }

    private fun initializeTradingAccountIfMissing() {
        if (saxoTradingAccountRepository.count() != 0L) {
            return
        }

        logger.info("Initializing Saxo trading account")

        val user = userRepository.findByUsername(initialUsername)
            ?: throw IllegalStateException("Initial user was not found")

        val saxoClientDetails = saxoApiClient.getSaxoClient()

        val initialOrderHistoryUrl =
            "$baseUrl/cs/v1/audit/orderactivities" +
                    "?\$top=200" +
                    "&EntryType=Last" +
                    "&ClientKey=${saxoClientDetails.clientKey}" +
                    "&AccountKey=${saxoClientDetails.defaultAccountKey}" +
                    "&FromDateTime=${Instant.now()}"

        saxoTradingAccountRepository.save(
            SaxoTradingAccount(
                saxoClient = SaxoClient(
                    user = user,
                    clientKey = saxoClientDetails.clientKey,
                ),
                saxoAccountKey = saxoClientDetails.defaultAccountKey,
                saxoAccountId = saxoClientDetails.defaultAccountId,
                orderHistoryNextPollUrl = initialOrderHistoryUrl
            )
        )
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