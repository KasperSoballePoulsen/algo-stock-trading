package dk.ksp.algotrading.enum

enum class OrderStatus(
    val saxoValue: String
) {
    PLACED("Placed"),
    FILLED("FinalFill"),
    REJECTED("Rejected");

    companion object {
        fun fromSaxoValue(value: String): OrderStatus =
            entries.firstOrNull { it.saxoValue == value }
                ?: throw IllegalArgumentException("Unknown Saxo order status: $value")
    }
}