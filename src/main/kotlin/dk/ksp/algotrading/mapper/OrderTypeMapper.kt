package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.enum.AccountTransactionType
import dk.ksp.algotrading.enum.BuySell

fun BuySell.toAccountTransactionType(): AccountTransactionType =
    when (this) {
        BuySell.BUY -> AccountTransactionType.BUY_ORDER
        BuySell.SELL -> AccountTransactionType.SELL_ORDER
    }