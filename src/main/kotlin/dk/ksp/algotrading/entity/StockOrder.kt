package dk.ksp.algotrading.entity

import dk.ksp.algotrading.enum.OrderType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "stock_orders")
class StockOrder(

    @ManyToOne
    @JoinColumn(name = "trader_id")
    val trader: StockTrader,

    val symbol: String,

    @Enumerated(EnumType.STRING)
    val type: OrderType,

    val quantity: Long,

    val price: BigDecimal,

    val timestamp: LocalDateTime = LocalDateTime.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)