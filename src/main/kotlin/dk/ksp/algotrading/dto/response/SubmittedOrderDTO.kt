package dk.ksp.algotrading.dto.response

import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.BuySell

data class SubmittedOrderDTO(
    val symbol: String,
    val quantity: Long,
    val buySell: BuySell,
    val status: OrderStatus
)