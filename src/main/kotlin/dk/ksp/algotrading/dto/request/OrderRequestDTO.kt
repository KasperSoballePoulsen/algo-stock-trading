package dk.ksp.algotrading.dto.request

import dk.ksp.algotrading.enum.BuySell
import java.math.BigDecimal

data class OrderRequestDTO(
    val symbol: String,
    val quantity: Long,
    val price: BigDecimal,
    val type: BuySell,
)