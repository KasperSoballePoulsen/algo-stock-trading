package dk.ksp.algotrading.dto.request

data class AddStockHoldingDTO(
    val symbol: String,
    val quantity: Long
)