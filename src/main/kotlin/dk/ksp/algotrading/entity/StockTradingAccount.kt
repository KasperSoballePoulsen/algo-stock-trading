package dk.ksp.algotrading.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "stock_trading_accounts")
class StockTradingAccount(

    @Column(nullable = false)
    var cashBalance: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "stock_trader_id", nullable = false)
    val stockTrader: StockTrader,

    var deletedAt: Instant? = null,

    @OneToMany(cascade = [CascadeType.PERSIST], mappedBy = "tradingAccount")
    var holdings: MutableList<StockHolding> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.PERSIST], mappedBy = "tradingAccount")
    var orders: MutableList<StockOrder> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var _id: Long? = null,

    ) {
    val id: Long
        get() = requireNotNull(_id) {
            "Cannot access id of a StockTradingAccount that has not been persisted"
        }
}