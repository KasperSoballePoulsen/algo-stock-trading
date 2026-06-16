package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.BrokerClient
import dk.ksp.algotrading.dto.response.OrderDTO
import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.BuySell
import dk.ksp.algotrading.exception.BrokerRejectedException
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TradingService(
    private val tradingAccountRepository: TradingAccountRepository,
    private val orderExecutionService: OrderExecutionService,
    private val brokerClient: BrokerClient,
    private val orderReservationService: OrderReservationService
) {

    fun createOrder(
        tradingAccountId: Long,
        symbol: String,
        quantity: Long,
        price: BigDecimal,
        type: BuySell
    ): OrderDTO {

        val tradingAccount = tradingAccountRepository.findActiveByIdWithTrader(tradingAccountId)
            ?: throw IllegalArgumentException("Trading account not found")

        val saxoBalances = brokerClient.getSaxoAccountBalances(
            tradingAccount.trader.saxoClientKey,
            tradingAccount.saxoAccountKey
        )

//        val createdOrder = orderReservationService.reserveOrder(
//            tradingAccountId,
//            symbol,
//            quantity,
//            price,
//            type,
//            saxoBalances.cashAvailableForTrading
//        )

        val status = try {
            brokerClient.sendOrder(
                tradingAccount.trader.saxoClientKey,
                tradingAccount.saxoAccountKey,
                symbol,
                quantity,
                type
            )
        } catch (ex: BrokerRejectedException) {
            OrderStatus.REJECTED
        }

        orderExecutionService.completeOrder(createdOrder.id, status)

        return OrderDTO(symbol, quantity, price, type, status)
    }

//    private fun validateOrder(
//        tradingAccount: TradingAccount,
//        symbol: String,
//        quantity: Long,
//        price: BigDecimal,
//        type: OrderType
//    ) {
//        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
//        require(quantity > 0) { "Quantity must be greater than 0" }
//        require(price > BigDecimal.ZERO) { "Price must be greater than 0" }
//
//        when (type) {
//            OrderType.BUY -> {
//                val totalPrice = price.multiply(quantity.toBigDecimal())
//
//                require(tradingAccount.cashAvailableForTrading >= totalPrice) {
//                    "Cannot buy for more money than owned"
//                }
//            }
//
//            OrderType.SELL -> {
//                val holding = holdingRepository
//                    .findActiveByAccountIdAndSymbol(tradingAccount.id, symbol.uppercase())
//                    ?: throw IllegalArgumentException("Cannot sell shares not owned")
//
//                require(holding.quantity >= quantity) {
//                    "Cannot sell more shares than owned"
//                }
//            }
//        }
//    }

}