package dk.ksp.algotrading.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class SaxoOrderErrorResponseDTO(
    @JsonProperty("Message")
    val message: String?,

    @JsonProperty("ErrorCode")
    val errorCode: String?,

    @JsonProperty("ModelState")
    val modelState: Map<String, List<String>>?
)