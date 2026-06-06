package dk.ksp.algotrading.dto.request

import dk.ksp.algotrading.enum.OrderType
import java.math.BigDecimal

data class StockOrderResultDTO(
    val symbol: String,
    val quantity: Long,
    val price: BigDecimal,
    val type: OrderType,
    val isCompleted: Boolean
)