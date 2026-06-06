package dk.ksp.algotrading.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable
import java.math.BigDecimal

data class StockPriceId(
    val symbol: String,
    val timestamp: Long
) : Serializable


@Entity
@Table(name = "stock_prices")
@IdClass(StockPriceId::class)
class StockPrice(
    val currentPrice: BigDecimal,
    val change: BigDecimal,
    val percentChange: BigDecimal,
    val highPrice: BigDecimal,
    val lowPrice: BigDecimal,
    val openPrice: BigDecimal,
    val previousClosePrice: BigDecimal,
    @Id
    val symbol: String,
    @Id
    val timestamp: Long,
)