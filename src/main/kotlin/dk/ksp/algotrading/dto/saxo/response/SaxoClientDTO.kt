package dk.ksp.algotrading.dto.saxo.response

import com.fasterxml.jackson.annotation.JsonProperty

data class SaxoClientDTO(
    @JsonProperty("ClientKey")
    val clientKey: String,
    @JsonProperty("DefaultAccountKey")
    val defaultAccountKey: String,
)