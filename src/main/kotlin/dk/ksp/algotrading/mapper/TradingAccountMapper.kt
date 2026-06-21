package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.TraderWithTradingAccountDTO
import dk.ksp.algotrading.dto.response.TradingAccountDTO
import dk.ksp.algotrading.entity.TradingAccount
import java.math.BigDecimal

fun TradingAccount.toTraderWithTradingAccountDTO(cashAvailableForTrading: BigDecimal) = TraderWithTradingAccountDTO(
    traderId = trader.id,
    username = trader.username,
    tradingAccount = toTradingAccountDTO(cashAvailableForTrading)
)


fun TradingAccount.toTradingAccountDTO(cashAvailableForTrading: BigDecimal) = TradingAccountDTO(
    accountId = id,
    cashAvailableForTrading = cashAvailableForTrading
)