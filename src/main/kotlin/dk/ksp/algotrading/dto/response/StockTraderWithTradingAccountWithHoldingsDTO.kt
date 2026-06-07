package dk.ksp.algotrading.dto.response

data class StockTraderWithTradingAccountWithHoldingsDTO(
    val id: Long,
    val username: String,
    val tradingAccount: StockTradingAccountWithHoldingsDTO
)