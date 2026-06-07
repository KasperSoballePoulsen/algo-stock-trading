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
import java.time.Instant

@Entity
@Table(name = "stock_orders")
class StockOrder(

    @Column(nullable = false)
    val symbol: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val type: OrderType,

    val quantity: Long,

    @Column(nullable = false)
    val price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "trading_account_id", nullable = false)
    val tradingAccount: StockTradingAccount,

    @Column(nullable = false)
    val timestamp: Instant = Instant.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

)