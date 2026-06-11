package dk.ksp.algotrading.service

import dk.ksp.algotrading.entity.Order
import dk.ksp.algotrading.entity.TradingAccount
import dk.ksp.algotrading.entity.TradingAccountTransaction
import dk.ksp.algotrading.enum.AccountTransactionType
import dk.ksp.algotrading.repository.TradingAccountRepository
import dk.ksp.algotrading.repository.TradingAccountTransactionRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class AccountTransactionService(
    private val tradingAccountTransactionRepository: TradingAccountTransactionRepository,
    private val tradingAccountRepository: TradingAccountRepository
) {
    fun createOrderAccountTransaction(
        tradingAccount: TradingAccount,
        type: AccountTransactionType,
        amount: BigDecimal,
        order: Order
    ) {
        if (type != AccountTransactionType.SELL_ORDER && type != AccountTransactionType.BUY_ORDER) {
            throw IllegalArgumentException("Only SELL or BUY order type allowed")
        }

        createAccountTransaction(tradingAccount, type, amount, order)
    }

    @Transactional
    fun createManualAccountTransaction(
        tradingAccountId: Long,
        type: AccountTransactionType,
        amount: BigDecimal
    ) {
        if (type != AccountTransactionType.DEPOSIT && type != AccountTransactionType.WITHDRAWAL) {
            throw IllegalArgumentException("Only deposit and withdrawal are allowed manually")
        }

        val tradingAccount = tradingAccountRepository.findActiveById(tradingAccountId)
            ?: throw IllegalArgumentException("Can not find TradingAccount")

        createAccountTransaction(tradingAccount, type, amount)
    }

    private fun createAccountTransaction(
        tradingAccount: TradingAccount,
        type: AccountTransactionType,
        amount: BigDecimal,
        order: Order? = null
    ) {

        val signedAmount = when (type) {
            AccountTransactionType.DEPOSIT,
            AccountTransactionType.SELL_ORDER -> amount

            AccountTransactionType.WITHDRAWAL,
            AccountTransactionType.BUY_ORDER -> amount.negate()
        }

        val newBalance = tradingAccount.cashAvailable + signedAmount
        tradingAccount.cashAvailable = newBalance

        tradingAccountTransactionRepository.save(
            TradingAccountTransaction(
                signedAmount,
                type,
                newBalance,
                tradingAccount,
                order
            )
        )
    }

}