package dk.ksp.algotrading.service

import dk.ksp.algotrading.entity.StockOrder
import dk.ksp.algotrading.entity.StockTradingAccount
import dk.ksp.algotrading.entity.StockTradingAccountTransaction
import dk.ksp.algotrading.enum.AccountTransactionType
import dk.ksp.algotrading.repository.StockTraderRepository
import dk.ksp.algotrading.repository.StockTradingAccountTransactionRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class AccountTransactionService(
    private val stockTradingAccountTransactionRepository: StockTradingAccountTransactionRepository,
    private val stockTraderRepository: StockTraderRepository
) {
    fun createOrderAccountTransaction(
        tradingAccount: StockTradingAccount,
        type: AccountTransactionType,
        amount: BigDecimal,
        stockOrder: StockOrder
    ) {
        if (type != AccountTransactionType.SELL_ORDER && type != AccountTransactionType.BUY_ORDER) {
            throw IllegalArgumentException("Only SELL or BUY order type allowed")
        }

        createAccountTransaction(tradingAccount, type, amount, stockOrder)
    }

    @Transactional
    fun createManualAccountTransaction(
        stockTraderId: Long,
        type: AccountTransactionType,
        amount: BigDecimal
    ) {
        if (type != AccountTransactionType.DEPOSIT && type != AccountTransactionType.WITHDRAWAL) {
            throw IllegalArgumentException("Only deposit and withdrawal are allowed manually")
        }

        val trader = stockTraderRepository.findByIdAndDeletedAtIsNullWithTradingAccount(stockTraderId)
            ?: throw IllegalArgumentException("Trader not found")

        val tradingAccount = trader.tradingAccount

        createAccountTransaction(tradingAccount, type, amount)
    }

    private fun createAccountTransaction(
        tradingAccount: StockTradingAccount,
        type: AccountTransactionType,
        amount: BigDecimal,
        stockOrder: StockOrder? = null
    ) {

        val signedAmount = when (type) {
            AccountTransactionType.DEPOSIT,
            AccountTransactionType.SELL_ORDER -> amount

            AccountTransactionType.WITHDRAWAL,
            AccountTransactionType.BUY_ORDER -> amount.negate()
        }

        val newBalance = tradingAccount.cashBalance + signedAmount
        tradingAccount.cashBalance = newBalance

        stockTradingAccountTransactionRepository.save(
            StockTradingAccountTransaction(
                amount = signedAmount,
                type = type,
                balanceAfter = newBalance,
                tradingAccount = tradingAccount,
                stockOrder = stockOrder
            )
        )
    }

}