package dk.ksp.algotrading.dto.response

data class PortfolioDTO(
    val accountId: Long,
    val holdings: List<HoldingDTO>
)