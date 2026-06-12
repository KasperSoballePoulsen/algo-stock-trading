package dk.ksp.algotrading.service

import dk.ksp.algotrading.entity.Holding
import dk.ksp.algotrading.entity.Order
import dk.ksp.algotrading.entity.TradingAccount
import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.OrderType
import dk.ksp.algotrading.mapper.toAccountTransactionType
import dk.ksp.algotrading.repository.HoldingRepository
import dk.ksp.algotrading.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class OrderExecutionService(
    private val holdingRepository: HoldingRepository,
    private val orderRepository: OrderRepository,
    private val accountTransactionService: AccountTransactionService,
) {

    @Transactional
    fun completeOrder(
        orderId: Long,
        orderStatus: OrderStatus
    ) {
        val order = orderRepository.findByIdWithTradingAccount(orderId)
            ?: throw IllegalArgumentException("Order not found")

        order.status = orderStatus

        if (orderStatus == OrderStatus.FILLED) {
            updateHoldings(
                order.tradingAccount,
                order.symbol,
                order.quantity,
                order.type
            )

            accountTransactionService.createOrderAccountTransaction(
                order.tradingAccount,
                order.type.toAccountTransactionType(),
                order.price.multiply(order.quantity.toBigDecimal()),
                order
            )
        }

        if (orderStatus == OrderStatus.REJECTED && order.type == OrderType.BUY) {
            order.tradingAccount.cashAvailableForTrading += order.price.multiply(order.quantity.toBigDecimal())
        }
    }


    private fun updateHoldings(
        tradingAccount: TradingAccount,
        symbol: String,
        quantity: Long,
        type: OrderType
    ) {

        val normalizedSymbol = symbol.uppercase()

        val existingHolding = holdingRepository.findActiveByAccountIdAndSymbol(tradingAccount.id, normalizedSymbol)

        when (type) {
            OrderType.BUY -> {
                if (existingHolding != null)
                    existingHolding.quantity += quantity
                else
                    holdingRepository.save(Holding(normalizedSymbol, quantity, tradingAccount))
            }

            OrderType.SELL -> {
                if (existingHolding == null)
                    throw IllegalArgumentException("Cannot sell shares not owned")

                if (existingHolding.quantity < quantity)
                    throw IllegalArgumentException("Cannot sell more shares than owned")

                existingHolding.quantity -= quantity

                if (existingHolding.quantity == 0L)
                    holdingRepository.delete(existingHolding)
            }
        }
    }
}