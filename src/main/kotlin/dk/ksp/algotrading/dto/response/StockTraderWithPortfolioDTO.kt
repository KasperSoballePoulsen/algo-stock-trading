package dk.ksp.algotrading.dto.response

data class StockTraderWithPortfolioDTO(
    val username: String,
    val portfolio: List<StockHoldingDTO>
)