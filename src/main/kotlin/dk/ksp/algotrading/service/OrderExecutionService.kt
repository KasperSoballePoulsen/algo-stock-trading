package dk.ksp.algotrading.service

import dk.ksp.algotrading.repository.HoldingRepository
import dk.ksp.algotrading.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderExecutionService(
    private val holdingRepository: HoldingRepository,
    private val orderRepository: OrderRepository,
) {

//    @Transactional
//    fun completeOrder(
//        orderId: Long,
//        orderStatus: OrderStatus
//    ) {
//        val order = orderRepository.findByIdWithTradingAccount(orderId)
//            ?: throw IllegalArgumentException("Order not found")
//
//        order.status = orderStatus
//
//        if (orderStatus == OrderStatus.FILLED) {
//            updateHoldings(
//                order.tradingAccount,
//                order.symbol,
//                order.quantity,
//                order.buySell
//            )
//
//            accountTransactionService.createOrderAccountTransaction(
//                order.tradingAccount,
//                order.buySell.toAccountTransactionType(),
//                order.price.multiply(order.quantity.toBigDecimal()),
//                order
//            )
//        }
//
//        if (orderStatus == OrderStatus.REJECTED && order.buySell == BuySell.BUY) {
//            order.tradingAccount.cashAvailableForTrading += order.price.multiply(order.quantity.toBigDecimal())
//        }
//    }


//    private fun updateHoldings(
//        tradingAccount: TradingAccount,
//        symbol: String,
//        quantity: Long,
//        type: BuySell
//    ) {
//
//        val normalizedSymbol = symbol.uppercase()
//
//        val existingHolding = holdingRepository.findActiveByAccountIdAndSymbol(tradingAccount.id, normalizedSymbol)
//
//        when (type) {
//            BuySell.BUY -> {
//                if (existingHolding != null)
//                    existingHolding.quantity += quantity
//                else
//                    holdingRepository.save(Holding(normalizedSymbol, quantity, tradingAccount))
//            }
//
//            BuySell.SELL -> {
//                if (existingHolding == null)
//                    throw IllegalArgumentException("Cannot sell shares not owned")
//
//                if (existingHolding.quantity < quantity)
//                    throw IllegalArgumentException("Cannot sell more shares than owned")
//
//                existingHolding.quantity -= quantity
//
//                if (existingHolding.quantity == 0L)
//                    holdingRepository.delete(existingHolding)
//            }
//        }
//    }
}