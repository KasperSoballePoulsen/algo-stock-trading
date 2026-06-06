package dk.ksp.algotrading.entity

import jakarta.persistence.Column
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

    @Column(nullable = false)
    val currentPrice: BigDecimal,

    @Column(nullable = false)
    val change: BigDecimal,

    @Column(nullable = false)
    val percentChange: BigDecimal,

    @Column(nullable = false)
    val highPrice: BigDecimal,

    @Column(nullable = false)
    val lowPrice: BigDecimal,

    @Column(nullable = false)
    val openPrice: BigDecimal,

    @Column(nullable = false)
    val previousClosePrice: BigDecimal,
    @Id
    val symbol: String,
    @Id
    val timestamp: Long,
)