package dk.ksp.algotrading.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "stock_holdings")
class StockHolding(

    @Column(nullable = false)
    val symbol: String,

    var quantity: Long,

    @ManyToOne
    @JoinColumn(name = "trading_account_id", nullable = false)
    val tradingAccount: StockTradingAccount,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var _id: Long? = null
) {
    val id: Long
        get() = requireNotNull(_id) {
            "Cannot access id of a StockHolding that has not been persisted"
        }
}