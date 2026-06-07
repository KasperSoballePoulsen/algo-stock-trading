package dk.ksp.algotrading.dto.response

import java.math.BigDecimal

data class StockTradingAccountWithHoldingsDTO(
    val cashBalance: BigDecimal,
    val holdings: List<StockHoldingDTO>
)