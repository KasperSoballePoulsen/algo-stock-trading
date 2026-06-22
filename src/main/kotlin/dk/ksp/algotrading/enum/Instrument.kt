package dk.ksp.algotrading.enum

enum class Instrument(
    val symbol: String,
    val uic: Long
) {
    AAPL("AAPL", 211),
    MSFT("MSFT", 27250),
    TSLA("TSLA", 113),
    NOVO("NOVO", 15629);

    companion object {
        fun fromSymbol(symbol: String): Long = entries.first { it.symbol == symbol }.uic

        fun fromUIC(uic: Long): String = entries.first { it.uic == uic }.symbol

    }
}