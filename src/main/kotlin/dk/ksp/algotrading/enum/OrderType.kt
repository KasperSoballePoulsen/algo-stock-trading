package dk.ksp.algotrading.enum


enum class OrderType(
    val saxoValue: String
) {
    MARKET("Market");

    companion object {
        fun fromSaxoValue(value: String): OrderType =
            OrderType.entries.firstOrNull { it.saxoValue == value }
                ?: throw IllegalArgumentException("Unknown Saxo order type: $value")
    }
}