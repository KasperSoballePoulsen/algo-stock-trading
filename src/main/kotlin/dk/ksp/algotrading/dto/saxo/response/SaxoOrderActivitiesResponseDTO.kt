package dk.ksp.algotrading.dto.saxo.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SaxoOrderActivitiesResponseDTO(
    @JsonProperty("Data")
    val data: List<SaxoOrderEventDTO>
)