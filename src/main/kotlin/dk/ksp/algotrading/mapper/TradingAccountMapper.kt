package dk.ksp.algotrading.mapper

import dk.ksp.algotrading.dto.response.PortfolioDTO
import dk.ksp.algotrading.dto.response.TraderDTO
import dk.ksp.algotrading.dto.response.TradingAccountDTO
import dk.ksp.algotrading.entity.Holding
import dk.ksp.algotrading.entity.SaxoTradingAccount
import java.math.BigDecimal

fun SaxoTradingAccount.toTraderWithTradingAccountDTO(cashAvailableForTrading: BigDecimal) = TraderDTO(
    userId = saxoClient.user.id,
    username = saxoClient.user.username,
    tradingAccount = toTradingAccountDTO(cashAvailableForTrading)
)


fun SaxoTradingAccount.toTradingAccountDTO(cashAvailableForTrading: BigDecimal) = TradingAccountDTO(
    accountId = id,
    cashAvailableForTrading = cashAvailableForTrading
)

fun SaxoTradingAccount.toPortfolioDTO(holdings: List<Holding>) = PortfolioDTO(
    accountId = id,
    holdings = holdings.toHoldingsDTO()
)