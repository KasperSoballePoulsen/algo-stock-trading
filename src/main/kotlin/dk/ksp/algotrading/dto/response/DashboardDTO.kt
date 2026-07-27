package dk.ksp.algotrading.dto.response

data class DashboardDTO(
    val account: TradingAccountDTO,
    val performance: PerformanceDTO,
    val holdings: List<HoldingDTO>,
)

data class PerformanceDTO(
    val today: Double,
    val todayPercent: Double,
    val total: Double,
    val totalPercent: Double,
)