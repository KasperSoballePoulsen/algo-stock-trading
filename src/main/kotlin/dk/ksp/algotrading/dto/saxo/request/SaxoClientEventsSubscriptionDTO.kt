package dk.ksp.algotrading.dto.saxo.request

import com.fasterxml.jackson.annotation.JsonProperty
import dk.ksp.algotrading.enum.SaxoEventActivity

data class SaxoClientEventsSubscriptionDTO(
    @JsonProperty("Arguments")
    val arguments: SaxoClientEventsSubscriptionArgumentsDTO,
    @JsonProperty("ContextId")
    val contextId: String,
    @JsonProperty("ReferenceId")
    val referenceId: String,
)

data class SaxoClientEventsSubscriptionArgumentsDTO(
    @JsonProperty("Activities")
    val activities: List<SaxoEventActivity>,
)
