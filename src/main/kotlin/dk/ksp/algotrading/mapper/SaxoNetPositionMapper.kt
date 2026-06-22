package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.SaxoNetPosition
import dk.ksp.algotrading.entity.Holding
import dk.ksp.algotrading.entity.TradingAccount
import dk.ksp.algotrading.enum.Instrument

fun List<SaxoNetPosition>.toHoldings(account: TradingAccount) = map { it.toHolding(account) }

fun SaxoNetPosition.toHolding(account: TradingAccount) = Holding(
    symbol = Instrument.fromUIC(netPositionBase.uic),
    quantity = netPositionBase.amount.toLong(),
    tradingAccount = account,
)