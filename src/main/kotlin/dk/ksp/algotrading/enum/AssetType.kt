package dk.ksp.algotrading.enum

import com.fasterxml.jackson.annotation.JsonValue

enum class AssetType(
    @get:JsonValue
    val saxoValue: String
) {
    STOCK("Stock")
}