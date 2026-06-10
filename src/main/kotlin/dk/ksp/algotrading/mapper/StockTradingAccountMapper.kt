package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.TradingAccountDTO
import dk.ksp.algotrading.dto.response.TradingAccountWithHoldingsDTO
import dk.ksp.algotrading.entity.TradingAccount

fun TradingAccount.toTradingAccountDTO() = TradingAccountDTO(id , cashBalance)

fun List<TradingAccount>.toTradingAccountsDTO() = map { it.toTradingAccountDTO() }

fun TradingAccount.toTradingAccountWithHoldingsDTO() =
    TradingAccountWithHoldingsDTO(id, cashBalance, holdings.toHoldingsDTO())