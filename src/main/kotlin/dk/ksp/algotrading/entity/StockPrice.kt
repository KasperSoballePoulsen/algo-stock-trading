package dk.ksp.algotrading.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "stock_prices")
class StockPrice(
    val currentPrice: Double,
    val change: Double,
    val percentChange: Double,
    val highPrice: Double,
    val lowPrice: Double,
    val openPrice: Double,
    val previousClosePrice: Double,
    @Id
    val timestamp: Long,
)