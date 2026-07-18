package dk.ksp.algotrading.enum


enum class DurationType(
    val saxoValue: String
) {
    DAY_ORDER("DayOrder");

    companion object {
        fun fromSaxoValue(value: String): DurationType =
            DurationType.entries.firstOrNull { it.saxoValue == value }
                ?: throw IllegalArgumentException("Unknown Saxo duration type: $value")
    }
}