package dk.ksp.algotrading.dto.response

import java.math.BigDecimal

data class TradingAccountDTO(
    val accountId: Long,
    val cashAvailableForTrading: BigDecimal,
)