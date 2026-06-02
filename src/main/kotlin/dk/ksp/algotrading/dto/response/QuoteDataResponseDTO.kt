package dk.ksp.algotrading.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class QuoteDataResponseDTO(

    @JsonProperty("c")
    val currentPrice: Double,

    @JsonProperty("d")
    val change: Double,

    @JsonProperty("dp")
    val percentChange: Double,

    @JsonProperty("h")
    val highPrice: Double,

    @JsonProperty("l")
    val lowPrice: Double,

    @JsonProperty("o")
    val openPrice: Double,

    @JsonProperty("pc")
    val previousClosePrice: Double,

    @JsonProperty("t")
    val timestamp: Long,
)