package dk.ksp.algotrading.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class SaxoOrderRequestDTO(
    @JsonProperty("AccountKey")
    val accountKey: String,

    @JsonProperty("Amount")
    val amount: Long,

    @JsonProperty("BuySell")
    val buySell: String,

    @JsonProperty("OrderType")
    val orderType: String,

    @JsonProperty("ManualOrder")
    val manualOrder: Boolean,

    @JsonProperty("Uic")
    val uic: Long,

    @JsonProperty("AssetType")
    val assetType: String,

    @JsonProperty("OrderDuration")
    val orderDuration: OrderDuration
)

data class OrderDuration(
    @JsonProperty("DurationType")
    val durationType: String
)