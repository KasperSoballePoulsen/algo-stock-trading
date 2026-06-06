package dk.ksp.algotrading.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "stock_holdings",
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = ["trader_id", "symbol"]
        )
    ]
)
class StockHolding(

    @ManyToOne
    @JoinColumn(name = "trader_id", nullable = false)
    val trader: StockTrader,

    @Column(nullable = false)
    val symbol: String,

    var quantity: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
)