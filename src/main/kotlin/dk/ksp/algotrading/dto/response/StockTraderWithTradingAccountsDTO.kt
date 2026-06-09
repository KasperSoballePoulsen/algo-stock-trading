package dk.ksp.algotrading.dto.response

data class StockTraderWithTradingAccountsDTO(
    val traderId: Long,
    val username: String,
    val tradingAccounts: List<StockTradingAccountDTO>
)