package dk.ksp.algotrading.dto.response

import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.OrderType
import java.math.BigDecimal

data class OrderDTO(
    val symbol: String,
    val quantity: Long,
    val price: BigDecimal,
    val type: OrderType,
    val status: OrderStatus
)