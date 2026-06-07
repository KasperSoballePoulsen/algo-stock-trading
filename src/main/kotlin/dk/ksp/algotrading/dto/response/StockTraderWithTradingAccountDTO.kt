package dk.ksp.algotrading.dto.response

data class StockTraderWithTradingAccountDTO(
    val id: Long,
    val username: String,
    val tradingAccount: StockTradingAccountDTO
)