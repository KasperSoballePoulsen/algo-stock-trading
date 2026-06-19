package dk.ksp.algotrading.dto.request

import dk.ksp.algotrading.enum.AssetType
import dk.ksp.algotrading.enum.BuySell
import dk.ksp.algotrading.enum.DurationType
import dk.ksp.algotrading.enum.OrderInitiator
import dk.ksp.algotrading.enum.OrderType

data class OrderRequestDTO(
    val symbol: String,
    val quantity: Long,
    val buySell: BuySell,
    val orderType: OrderType,
    val initiator: OrderInitiator,
    val assetType: AssetType,
    val durationType: DurationType
)