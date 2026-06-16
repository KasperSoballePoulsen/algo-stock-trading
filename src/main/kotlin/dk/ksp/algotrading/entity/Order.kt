package dk.ksp.algotrading.entity

import dk.ksp.algotrading.enum.OrderStatus
import dk.ksp.algotrading.enum.BuySell
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
@Table(name = "orders")
class Order(

    @Column(nullable = false)
    val symbol: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val buySell: BuySell,

    val quantity: Long,

    @Column
    val executedPrice: BigDecimal?,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: OrderStatus,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var orderType: OrderType,

    @ManyToOne
    @JoinColumn(name = "trading_account_id", nullable = false)
    val tradingAccount: TradingAccount,

    @Column(nullable = false)
    val timestamp: Instant = Instant.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var _id: Long? = null
) {
    val id: Long
        get() = requireNotNull(_id) {
            "Cannot access id of a Order that has not been persisted"
        }
}