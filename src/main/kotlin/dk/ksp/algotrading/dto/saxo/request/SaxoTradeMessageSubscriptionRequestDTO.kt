package dk.ksp.algotrading.dto.saxo.request

import com.fasterxml.jackson.annotation.JsonProperty

class SaxoTradeMessageSubscriptionRequestDTO(
    @JsonProperty("ContextId")
    val contextId: String,

    @JsonProperty("ReferenceId")
    val referenceId: String
)