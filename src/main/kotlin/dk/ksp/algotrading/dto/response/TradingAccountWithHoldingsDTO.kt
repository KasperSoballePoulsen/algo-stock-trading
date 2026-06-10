package dk.ksp.algotrading.dto.response

import java.math.BigDecimal

data class TradingAccountWithHoldingsDTO(
    val accountId: Long,
    val cashBalance: BigDecimal,
    val holdings: List<HoldingDTO>
)