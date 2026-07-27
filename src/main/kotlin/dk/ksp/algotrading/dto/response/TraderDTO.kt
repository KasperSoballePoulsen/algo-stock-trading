package dk.ksp.algotrading.dto.response

data class TraderDTO(
    val userId: Long,
    val username: String,
    val tradingAccount: TradingAccountDTO
)