package dk.ksp.algotrading.enum

enum class OrderStatus {
    CREATED,
    SUBMITTED,
    PARTIALLY_FILLED,
    FILLED,
    REJECTED,
    CANCELLED,
    FAILED
}