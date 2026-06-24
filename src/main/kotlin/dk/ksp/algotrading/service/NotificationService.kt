package dk.ksp.algotrading.service


import dk.ksp.algotrading.client.NotificationClient
import org.springframework.stereotype.Service


@Service
class NotificationService(
    private val notificationClient: NotificationClient,
) {
    fun sendNotification(message: String, title: String) {
        notificationClient.sendNotification(message, title)
    }



//    private val logger = LoggerFactory.getLogger(javaClass)


//    @Scheduled(cron = "0 0 22 * * MON-FRI", zone = "Europe/Copenhagen")
//    fun calculatePortfolioDailyPercentChange() {
//        val traders = traderRepository.findAllActiveWithTradingAccounts()
//
//        val portfolioChanges = traders.map { trader ->
//            try {
//                val holdings = trader.tradingAccounts.flatMap { account ->
//                    holdingRepository.findAllActiveByAccountId(account.id)
//                }
//
//                if (holdings.isEmpty()) {
//                    return@map "${trader.username}: No holdings"
//                }
//
//                val pricesBySymbol = holdings
//                    .map { it.symbol }
//                    .distinct()
//                    .map { symbol ->
//                        marketDataClient.fetchRealTimeQuoteData(symbol).toStockPrice(symbol)
//                    }
//                    .associateBy { it.symbol }
//
//                val values = holdings.map {
//                    val price = pricesBySymbol[it.symbol]
//                        ?: throw IllegalStateException("Missing price for ${it.symbol}")
//
//                    Pair(
//                        it.quantity.toBigDecimal() * price.previousClosePrice,
//                        it.quantity.toBigDecimal() * price.currentPrice
//                    )
//                }
//
//                val yesterdaysValue = values.sumOf { it.first }
//                val todaysValue = values.sumOf { it.second }
//
//                if (yesterdaysValue.compareTo(BigDecimal.ZERO) == 0) {
//                    return@map "${trader.username}: No previous portfolio value"
//                }
//
//                val percentChange = todaysValue
//                    .subtract(yesterdaysValue)
//                    .divide(yesterdaysValue, 4, RoundingMode.HALF_UP)
//                    .multiply(100.toBigDecimal())
//                    .setScale(2, RoundingMode.HALF_UP)
//
//                "${trader.username}: $percentChange%"
//            } catch (e: Exception) {
//                logger.error("Failed to calculate portfolio for {}", trader.username, e)
//                "${trader.username}: ERROR"
//            }
//        }
//
//        val message = portfolioChanges.joinToString("\n")
//
//        notificationClient.sendNotification(message, "Portfolio Daily Change")
//    }
}