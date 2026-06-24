package dk.ksp.algotrading.dto.saxo.response

import com.fasterxml.jackson.annotation.JsonProperty

data class SaxoOrderSuccessResponseDTO(
    @JsonProperty("OrderId")
    val orderId: String
)