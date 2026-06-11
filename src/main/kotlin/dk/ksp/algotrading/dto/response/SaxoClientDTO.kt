package dk.ksp.algotrading.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class SaxoClientDTO(
    @JsonProperty("ClientKey")
    val clientKey: String,
    @JsonProperty("DefaultAccountKey")
    val defaultAccountKey: String,
)