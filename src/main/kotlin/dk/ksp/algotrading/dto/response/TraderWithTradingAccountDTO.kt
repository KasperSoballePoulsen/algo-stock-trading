package dk.ksp.algotrading.dto.response

data class TraderWithTradingAccountDTO(
    val traderId: Long,
    val username: String,
    val tradingAccount: TradingAccountDTO
)