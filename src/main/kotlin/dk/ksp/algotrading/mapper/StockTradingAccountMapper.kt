package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.TradingAccountDTO
import dk.ksp.algotrading.dto.response.TradingAccountWithHoldingsDTO
import dk.ksp.algotrading.entity.TradingAccount

fun TradingAccount.toTradingAccountDTO() = TradingAccountDTO(id , cashAvailable)

fun List<TradingAccount>.toTradingAccountsDTO() = map { it.toTradingAccountDTO() }

fun TradingAccount.toTradingAccountWithHoldingsDTO() =
    TradingAccountWithHoldingsDTO(id, cashAvailable, holdings.toHoldingsDTO())