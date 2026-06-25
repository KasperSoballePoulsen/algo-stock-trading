package dk.ksp.algotrading.dto.saxo.request

import com.fasterxml.jackson.annotation.JsonProperty
import dk.ksp.algotrading.enum.AssetType
import dk.ksp.algotrading.enum.BuySell
import dk.ksp.algotrading.enum.DurationType
import dk.ksp.algotrading.enum.OrderType

data class SaxoOrderRequestDTO(
    @JsonProperty("AccountKey")
    val accountKey: String,

    @JsonProperty("Amount")
    val amount: Long,

    @JsonProperty("BuySell")
    val buySell: BuySell,

    @JsonProperty("OrderType")
    val orderType: OrderType,

    @JsonProperty("ManualOrder")
    val manualOrder: Boolean,

    @JsonProperty("Uic")
    val uic: Long,

    @JsonProperty("AssetType")
    val assetType: AssetType,

    @JsonProperty("OrderDuration")
    val orderDuration: OrderDuration
)

data class OrderDuration(
    @JsonProperty("DurationType")
    val durationType: DurationType
)