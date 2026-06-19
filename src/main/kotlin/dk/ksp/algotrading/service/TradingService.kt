package dk.ksp.algotrading.service

import dk.ksp.algotrading.client.BrokerClient
import dk.ksp.algotrading.dto.request.OrderDuration
import dk.ksp.algotrading.dto.response.OrderDTO
import dk.ksp.algotrading.enum.AssetType
import dk.ksp.algotrading.enum.BuySell
import dk.ksp.algotrading.enum.DurationType
import dk.ksp.algotrading.enum.Instrument
import dk.ksp.algotrading.enum.OrderInitiator
import dk.ksp.algotrading.enum.OrderType
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.springframework.stereotype.Service

@Service
class TradingService(
    private val tradingAccountRepository: TradingAccountRepository,
    private val orderExecutionService: OrderExecutionService,
    private val brokerClient: BrokerClient,
) {

    fun createOrder(
        tradingAccountId: Long,
        symbol: String,
        quantity: Long,
        buySell: BuySell,
        orderType: OrderType,
        initiator: OrderInitiator
    ): OrderDTO {

        val tradingAccount = tradingAccountRepository.findActiveById(tradingAccountId)
            ?: throw IllegalArgumentException("Trading account not found")

        if (quantity <= 0) throw IllegalArgumentException("Quantity must be positive")
        if (symbol.isBlank()) throw IllegalArgumentException("Symbol is required")

        val isManualOrder = when (initiator) {
            OrderInitiator.HUMAN -> true
            OrderInitiator.ALGORITHM -> false
        }
        val uic = Instrument.fromSymbol(symbol.uppercase())

        val orderId = brokerClient.sendOrder(
            tradingAccount.saxoAccountKey,
            quantity,
            buySell,
            orderType,
            isManualOrder,
            uic,
            AssetType.STOCK.saxoValue,
            OrderDuration(DurationType.DAY_ORDER.saxoValue)
        )


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