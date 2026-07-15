package dk.ksp.algotrading.enum

enum class BuySell(
    val saxoValue: String
) {
    BUY("Buy"),
    SELL("Sell");

    companion object {
        fun fromSaxoValue(value: String): BuySell =
            entries.firstOrNull { it.saxoValue == value }
                ?: throw IllegalArgumentException("Unknown Saxo BuySell value: $value")
    }
}