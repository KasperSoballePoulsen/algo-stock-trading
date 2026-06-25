package dk.ksp.algotrading.enum

import com.fasterxml.jackson.annotation.JsonValue

enum class BuySell(
    @get:JsonValue
    val saxoValue: String
) {
    BUY("Buy"),
    SELL("Sell")
}