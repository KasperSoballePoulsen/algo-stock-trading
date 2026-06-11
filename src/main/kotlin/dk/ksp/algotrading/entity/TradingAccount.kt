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
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "trading_accounts")
class TradingAccount(

    @Column(nullable = false)
    var cashAvailable: BigDecimal,

    @Column(nullable = false)
    val saxoAccountKey: String,

    @ManyToOne
    @JoinColumn(name = "trader_id", nullable = false)
    val trader: Trader,

    var deletedAt: Instant? = null,

    @OneToMany(cascade = [CascadeType.PERSIST], mappedBy = "tradingAccount")
    var holdings: MutableList<Holding> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.PERSIST], mappedBy = "tradingAccount")
    var orders: MutableList<Order> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var _id: Long? = null,

    ) {
    val id: Long
        get() = requireNotNull(_id) {
            "Cannot access id of a TradingAccount that has not been persisted"
        }
}