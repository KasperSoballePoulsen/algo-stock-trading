package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.BrokerClient
import dk.ksp.algotrading.dto.request.StockOrderResultDTO
import dk.ksp.algotrading.entity.StockTradingAccount
import dk.ksp.algotrading.enum.OrderType
import dk.ksp.algotrading.repository.StockHoldingRepository
import dk.ksp.algotrading.repository.StockTradingAccountRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class StockTradingService(
    private val stockTradingAccountRepository: StockTradingAccountRepository,
    private val stockHoldingRepository: StockHoldingRepository,
    private val orderExecutionService: OrderExecutionService,
    private val brokerClient: BrokerClient,
) {

    fun createOrder(
        tradingAccountId: Long,
        symbol: String,
        quantity: Long,
        price: BigDecimal,
        type: OrderType
    ): StockOrderResultDTO {

        val tradingAccount = stockTradingAccountRepository.findActiveById(tradingAccountId)
            ?: throw IllegalArgumentException("Trading account not found")

        validateOrder(tradingAccount, symbol, quantity, price, type)
        val isCompleted = brokerClient.sendOrder(tradingAccount, symbol, quantity, price, type)
        if (isCompleted) {
            orderExecutionService.completeOrder(tradingAccount, symbol, quantity, price, type)
        }

        return StockOrderResultDTO(symbol, quantity, price, type, isCompleted)
    }

    private fun validateOrder(
        tradingAccount: StockTradingAccount,
        symbol: String,
        quantity: Long,
        price: BigDecimal,
        type: OrderType
    ) {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
        require(quantity > 0) { "Quantity must be greater than 0" }
        require(price > BigDecimal.ZERO) { "Price must be greater than 0" }

        when (type) {
            OrderType.BUY -> {
                val totalPrice = BigDecimal.valueOf(quantity).multiply(price)

                require(tradingAccount.cashBalance >= totalPrice) {
                    "Cannot buy for more money than owned"
                }
            }

            OrderType.SELL -> {
                val holding = stockHoldingRepository
                    .findActiveByAccountIdAndSymbol(tradingAccount.id, symbol.uppercase())
                    ?: throw IllegalArgumentException("Cannot sell shares not owned")

                require(holding.quantity >= quantity) {
                    "Cannot sell more shares than owned"
                }
            }
        }
    }

}