package dk.ksp.algotrading.dto.saxo.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import dk.ksp.algotrading.enum.BuySell
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
data class SaxoOrderEventDTO(
    @JsonProperty("AccountId")
    val accountId: String,

    @JsonProperty("ActivityTime")
    val activityTime: Instant,

    @JsonProperty("Amount")
    val amount: Double,

    @JsonProperty("AssetType")
    val assetType: String,

    @JsonProperty("BuySell")
    val buySell: String,

    @JsonProperty("ClientId")
    val clientId: String,

    @JsonProperty("CorrelationKey")
    val correlationKey: String,

    @JsonProperty("Duration")
    val duration: SaxoDurationDTO,

    @JsonProperty("HandledBy")
    val handledBy: String,

    @JsonProperty("OrderId")
    val orderId: String,

    @JsonProperty("OrderRelation")
    val orderRelation: String,

    @JsonProperty("OrderType")
    val orderType: String,

    @JsonProperty("Status")
    val status: String,

    @JsonProperty("SubStatus")
    val subStatus: String,

    @JsonProperty("Uic")
    val uic: Long,

    @JsonProperty("AveragePrice")
    val averagePrice: BigDecimal? = null,

    @JsonProperty("ExecutionPrice")
    val executionPrice: BigDecimal? = null,

    @JsonProperty("FillAmount")
    val fillAmount: BigDecimal? = null,

    @JsonProperty("FilledAmount")
    val filledAmount: BigDecimal? = null,

    @JsonProperty("PositionId")
    val positionId: String? = null,

    @JsonProperty("ValueDate")
    val valueDate: LocalDate? = null,

    @JsonProperty("Venue")
    val venue: String? = null
) : SaxoStreamEvent

data class SaxoDurationDTO(
    @JsonProperty("DurationType")
    val durationType: String,
)