package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.BrokerClient
import dk.ksp.algotrading.dto.response.OrderDTO
import dk.ksp.algotrading.entity.TradingAccount
import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.OrderType
import dk.ksp.algotrading.repository.HoldingRepository
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TradingService(
    private val tradingAccountRepository: TradingAccountRepository,
    private val holdingRepository: HoldingRepository,
    private val orderExecutionService: OrderExecutionService,
    private val brokerClient: BrokerClient,
) {

    fun createOrder(
        tradingAccountId: Long,
        symbol: String,
        quantity: Long,
        price: BigDecimal,
        type: OrderType
    ): OrderDTO {

        val tradingAccount = tradingAccountRepository.findActiveById(tradingAccountId)
            ?: throw IllegalArgumentException("Trading account not found")

        validateOrder(tradingAccount, symbol, quantity, price, type)
        val status = brokerClient.sendOrder(tradingAccount, symbol, quantity, price, type)

        if (status == OrderStatus.FILLED) {
            orderExecutionService.completeOrder(tradingAccount, symbol, quantity, price, type, status)
            return OrderDTO(symbol, quantity, price, type, OrderStatus.FILLED)
        }

        return OrderDTO(symbol, quantity, price, type, OrderStatus.REJECTED)
    }

    private fun validateOrder(
        tradingAccount: TradingAccount,
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
                val holding = holdingRepository
                    .findActiveByAccountIdAndSymbol(tradingAccount.id, symbol.uppercase())
                    ?: throw IllegalArgumentException("Cannot sell shares not owned")

                require(holding.quantity >= quantity) {
                    "Cannot sell more shares than owned"
                }
            }
        }
    }

}