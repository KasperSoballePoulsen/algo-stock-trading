package dk.ksp.algotrading.dto.saxo.response

import com.fasterxml.jackson.annotation.JsonProperty

data class SaxoOrderErrorResponseDTO(
    @JsonProperty("Message")
    val message: String? = null,

    @JsonProperty("ErrorCode")
    val errorCode: String? = null,

    @JsonProperty("ModelState")
    val modelState: Map<String, List<String>>? = null,

    @JsonProperty("ErrorInfo")
    val errorInfo: SaxoErrorInfoDTO? = null
) {
    val resolvedMessage: String
        get() = message ?: errorInfo?.message ?: "Saxo rejected order"

    val resolvedErrorCode: String?
        get() = errorCode ?: errorInfo?.errorCode
}

data class SaxoErrorInfoDTO(
    @JsonProperty("ErrorCode")
    val errorCode: String? = null,

    @JsonProperty("Message")
    val message: String? = null
)