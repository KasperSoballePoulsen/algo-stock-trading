package dk.ksp.algotrading.dto.response


data class TradingAccountWithHoldingsDTO(
    val accountId: Long,
    val holdings: List<HoldingDTO>
)