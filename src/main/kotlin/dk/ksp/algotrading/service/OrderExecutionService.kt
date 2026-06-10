package dk.ksp.algotrading.service

import dk.ksp.algotrading.entity.Holding
import dk.ksp.algotrading.entity.Order
import dk.ksp.algotrading.entity.TradingAccount
import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.OrderType
import dk.ksp.algotrading.mapper.toAccountTransactionType
import dk.ksp.algotrading.repository.HoldingRepository
import dk.ksp.algotrading.repository.OrderRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderExecutionService(
    private val holdingRepository: HoldingRepository,
    private val orderRepository: OrderRepository,
    private val accountTransactionService: AccountTransactionService,
) {

    @Transactional
    fun completeOrder(
        tradingAccount: TradingAccount,
        symbol: String,
        quantity: Long,
        price: BigDecimal,
        type: OrderType,
        orderStatus: OrderStatus
    ) {
        val order = orderRepository.save(Order(symbol, type, quantity, price, orderStatus, tradingAccount))
        updateHoldings(tradingAccount, symbol, quantity, type)
        accountTransactionService.createOrderAccountTransaction(
            tradingAccount,
            type.toAccountTransactionType(),
            price.multiply(quantity.toBigDecimal()),
            order
        )
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