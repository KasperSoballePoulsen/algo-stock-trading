package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.TraderDTO
import dk.ksp.algotrading.dto.response.TraderWithTradingAccountsDTO
import dk.ksp.algotrading.entity.Trader

fun Trader.toTraderDTO() = TraderDTO(id, username)

fun Trader.toTraderWithTradingAccountsDTO() =
    TraderWithTradingAccountsDTO(id, username, tradingAccounts.toTradingAccountsDTO())

fun List<Trader>.toTraderDTOs() = map { it.toTraderDTO() }