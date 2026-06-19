package dk.ksp.algotrading.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class SaxoOrderSuccessResponseDTO(
    @JsonProperty("OrderId")
    val orderId: Long
)