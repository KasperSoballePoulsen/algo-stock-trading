package dk.ksp.algotrading.enum

import com.fasterxml.jackson.annotation.JsonValue

enum class DurationType(
    @get:JsonValue
    val saxoValue: String
) {
    DAY_ORDER("DayOrder")
}