package dk.ksp.algotrading.dto.request

import dk.ksp.algotrading.enum.BuySell
import dk.ksp.algotrading.enum.OrderInitiator
import dk.ksp.algotrading.enum.OrderType
import java.math.BigDecimal

data class OrderRequestDTO(
    val symbol: String,
    val quantity: Long,
    val buySell: BuySell,
    val orderType: OrderType,
    val initiator: OrderInitiator
)