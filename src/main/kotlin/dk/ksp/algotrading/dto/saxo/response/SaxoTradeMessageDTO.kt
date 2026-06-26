package dk.ksp.algotrading.dto.saxo.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

@JsonIgnoreProperties(ignoreUnknown = true)
data class SaxoTradeMessageDTO(
    @JsonProperty("AccountId")
    val accountId: String,

    @JsonProperty("DateTime")
    val dateTime: Instant,

    @JsonProperty("IsDiscardable")
    val isDiscardable: Boolean,

    @JsonProperty("MessageBody")
    val messageBody: String,

    @JsonProperty("MessageHeader")
    val messageHeader: String,

    @JsonProperty("MessageId")
    val messageId: String,

    @JsonProperty("MessageType")
    val messageType: String,

    @JsonProperty("OrderId")
    val orderId: String? = null,

    @JsonProperty("PositionId")
    val positionId: String? = null,

    @JsonProperty("SourceOrderId")
    val sourceOrderId: String? = null
) : SaxoStreamEvent