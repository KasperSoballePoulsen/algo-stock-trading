package dk.ksp.algotrading.dto.response

data class TraderDTO(
    val id: Long,
    val username: String,
    val tradingAccount: TradingAccountDTO
)