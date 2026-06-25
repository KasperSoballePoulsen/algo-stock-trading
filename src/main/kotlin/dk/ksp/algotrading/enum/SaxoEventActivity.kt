package dk.ksp.algotrading.enum

import com.fasterxml.jackson.annotation.JsonValue

enum class SaxoEventActivity(
    @get:JsonValue
    val saxoValue: String
) {
    ORDERS("Orders"),
}