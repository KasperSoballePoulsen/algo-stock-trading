package dk.ksp.algotrading.dto.request

import dk.ksp.algotrading.enum.AssetType
import dk.ksp.algotrading.enum.BuySell
import dk.ksp.algotrading.enum.DurationType
import dk.ksp.algotrading.enum.OrderInitiator
import dk.ksp.algotrading.enum.OrderType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class OrderRequestDTO(
    @field:NotBlank(message = "Symbol is required")
    val symbol: String,

    @field:Positive(message = "Quantity must be positive")
    val quantity: Long,
    val buySell: BuySell,
    val orderType: OrderType,
    val initiator: OrderInitiator,
    val assetType: AssetType,
    val durationType: DurationType
)