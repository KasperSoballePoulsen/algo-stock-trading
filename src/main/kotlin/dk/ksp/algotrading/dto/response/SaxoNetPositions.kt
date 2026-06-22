package dk.ksp.algotrading.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class SaxoNetPositionsResponse(
    @JsonProperty("__count")
    val count: Long,

    @JsonProperty("Data")
    val data: List<SaxoNetPosition>
)

data class SaxoNetPosition(
    @JsonProperty("NetPositionBase")
    val netPositionBase: NetPositionBase
)

data class NetPositionBase(
    @JsonProperty("Amount")
    val amount: Double,

    @JsonProperty("Uic")
    val uic: Long
)
