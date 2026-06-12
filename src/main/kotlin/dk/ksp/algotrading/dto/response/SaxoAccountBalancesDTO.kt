package dk.ksp.algotrading.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class SaxoAccountBalancesDTO(
    @JsonProperty("CashAvailableForTrading")
    val cashAvailableForTrading: BigDecimal,
)
