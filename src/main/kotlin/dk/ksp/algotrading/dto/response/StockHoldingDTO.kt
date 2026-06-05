package dk.ksp.algotrading.dto.response

data class StockHoldingDTO(
    val symbol: String,
    val quantity: Long,
)