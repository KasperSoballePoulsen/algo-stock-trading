package dk.ksp.algotrading.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class QuoteDataDTO(

    @JsonProperty("c")
    val currentPrice: BigDecimal,

    @JsonProperty("d")
    val change: BigDecimal,

    @JsonProperty("dp")
    val percentChange: BigDecimal,

    @JsonProperty("h")
    val highPrice: BigDecimal,

    @JsonProperty("l")
    val lowPrice: BigDecimal,

    @JsonProperty("o")
    val openPrice: BigDecimal,

    @JsonProperty("pc")
    val previousClosePrice: BigDecimal,

    @JsonProperty("t")
    val timestamp: Long,
)