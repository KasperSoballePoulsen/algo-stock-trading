package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.saxo.response.SaxoNetPosition
import dk.ksp.algotrading.entity.Holding
import dk.ksp.algotrading.entity.SaxoTradingAccount
import dk.ksp.algotrading.enum.Instrument

fun List<SaxoNetPosition>.toHoldings(account: SaxoTradingAccount) = map { it.toHolding(account) }

fun SaxoNetPosition.toHolding(account: SaxoTradingAccount) = Holding(
    symbol = Instrument.fromUIC(netPositionBase.uic),
    quantity = netPositionBase.amount.toLong(),
    saxoTradingAccount = account,
)