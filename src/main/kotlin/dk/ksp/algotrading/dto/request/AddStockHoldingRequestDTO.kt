package dk.ksp.algotrading.dto.request

data class AddStockHoldingRequestDTO(
    val symbol: String,
    val quantity: Long
)