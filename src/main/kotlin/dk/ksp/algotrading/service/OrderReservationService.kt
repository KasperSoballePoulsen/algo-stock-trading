package dk.ksp.algotrading.service

import dk.ksp.algotrading.entity.Order
import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.BuySell
import dk.ksp.algotrading.repository.OrderRepository
import dk.ksp.algotrading.repository.TradingAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class OrderReservationService(
    private val tradingAccountRepository: TradingAccountRepository,
    private val orderRepository: OrderRepository
) {

//    @Transactional
//    fun reserveOrder(
//        tradingAccountId: Long,
//        symbol: String,
//        quantity: Long,
//        price: BigDecimal,
//        type: BuySell,
//        cashAvailableForTrading: BigDecimal
//    ): Order {
//        val tradingAccount = tradingAccountRepository.findActiveByIdForUpdate(tradingAccountId)
//            ?: throw IllegalArgumentException("Trading account not found")
//
//        tradingAccount.cashAvailableForTrading = cashAvailableForTrading
//
//        val orderValue = price.multiply(quantity.toBigDecimal())
//
//        if (type == BuySell.BUY) {
//            if (tradingAccount.cashAvailableForTrading < orderValue) {
//                throw IllegalArgumentException("Insufficient funds")
//            }
//
//            tradingAccount.cashAvailableForTrading -= orderValue
//        }
//
//        return orderRepository.save(
//            Order(symbol, type, quantity, price, OrderStatus.SUBMITTED, tradingAccount)
//        )
//    }
}