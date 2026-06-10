package dk.ksp.algotrading.dto.response

data class TraderWithTradingAccountsDTO(
    val traderId: Long,
    val username: String,
    val tradingAccounts: List<TradingAccountDTO>
)