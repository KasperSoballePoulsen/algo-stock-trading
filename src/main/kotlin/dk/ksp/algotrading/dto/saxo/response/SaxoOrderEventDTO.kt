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

    @JsonProperty("Amount")
    val amount: Double,

    @JsonProperty("BuySell")
    val buySell: String,

    @JsonProperty("Duration")
    val duration: SaxoDurationDTO,

    @JsonProperty("OrderId")
    val orderId: String,

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

) : SaxoStreamEvent

data class SaxoDurationDTO(
    @JsonProperty("DurationType")
    val durationType: String,
)