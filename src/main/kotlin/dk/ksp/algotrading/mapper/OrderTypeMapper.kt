package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.enum.AccountTransactionType
import dk.ksp.algotrading.enum.OrderType

fun OrderType.toAccountTransactionType(): AccountTransactionType =
    when (this) {
        OrderType.BUY -> AccountTransactionType.BUY_ORDER
        OrderType.SELL -> AccountTransactionType.SELL_ORDER
    }