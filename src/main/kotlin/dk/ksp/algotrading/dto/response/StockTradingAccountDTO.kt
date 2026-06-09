package dk.ksp.algotrading.dto.response

import java.math.BigDecimal

data class StockTradingAccountDTO(
    val accountId: Long,
    val cashBalance: BigDecimal,
)