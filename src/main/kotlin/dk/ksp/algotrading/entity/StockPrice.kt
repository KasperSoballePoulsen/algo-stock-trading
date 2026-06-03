package dk.ksp.algotrading.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "stock_prices")
class StockPrice(
    val currentPrice: BigDecimal,
    val change: BigDecimal,
    val percentChange: BigDecimal,
    val highPrice: BigDecimal,
    val lowPrice: BigDecimal,
    val openPrice: BigDecimal,
    val previousClosePrice: BigDecimal,
    @Id
    val timestamp: Long,
)