package dk.ksp.algotrading.service

import dk.ksp.algotrading.entity.StockHolding
import dk.ksp.algotrading.entity.StockOrder
import dk.ksp.algotrading.entity.StockTradingAccount
import dk.ksp.algotrading.enum.OrderType
import dk.ksp.algotrading.mapper.toAccountTransactionType
import dk.ksp.algotrading.repository.StockHoldingRepository
import dk.ksp.algotrading.repository.StockOrderRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderExecutionService(
    private val stockHoldingRepository: StockHoldingRepository,
    private val stockOrderRepository: StockOrderRepository,
    private val accountTransactionService: AccountTransactionService,
) {

    @Transactional
    fun completeOrder(
        tradingAccount: StockTradingAccount,
        symbol: String,
        quantity: Long,
        price: BigDecimal,
        type: OrderType
    ) {
        val stockOrder = stockOrderRepository.save(StockOrder(symbol, type, quantity, price, tradingAccount))
        updateHoldings(tradingAccount, symbol, quantity, type)
        accountTransactionService.createOrderAccountTransaction(
            tradingAccount,
            type.toAccountTransactionType(),
            price.multiply(quantity.toBigDecimal()),
            stockOrder
        )
    }


    private fun updateHoldings(
        tradingAccount: StockTradingAccount,
        symbol: String,
        quantity: Long,
        type: OrderType
    ) {

        val normalizedSymbol = symbol.uppercase()

        val existingHolding = stockHoldingRepository.findActiveByAccountIdAndSymbol(tradingAccount.id, normalizedSymbol)

        when (type) {
            OrderType.BUY -> {
                if (existingHolding != null)
                    existingHolding.quantity += quantity
                else
                    stockHoldingRepository.save(StockHolding(normalizedSymbol, quantity, tradingAccount))
            }

            OrderType.SELL -> {
                if (existingHolding == null)
                    throw IllegalArgumentException("Cannot sell shares not owned")

                if (existingHolding.quantity < quantity)
                    throw IllegalArgumentException("Cannot sell more shares than owned")

                existingHolding.quantity -= quantity

                if (existingHolding.quantity == 0L)
                    stockHoldingRepository.delete(existingHolding)
            }
        }
    }
}