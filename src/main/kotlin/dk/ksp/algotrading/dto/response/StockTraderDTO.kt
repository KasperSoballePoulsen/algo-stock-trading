package dk.ksp.algotrading.dto.response


data class StockTraderDTO(
    val username: String,
)

data class StockTraderWithPortfolioDTO(
    val username: String,
    val portfolio: List<StockHoldingDTO>
)
