package dk.ksp.algotrading.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "stock_traders")
class StockTrader protected constructor(

    @Column(nullable = false, unique = true)
    val username: String,

    @OneToMany(mappedBy = "stockTrader", cascade = [CascadeType.PERSIST])
    val tradingAccounts: MutableList<StockTradingAccount> = mutableListOf(),

    var deletedAt: Instant? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var _id: Long? = null
) {
    val id: Long
        get() = requireNotNull(_id) {
            "Cannot access id of a StockTrader that has not been persisted"
        }

    companion object {
        fun create(username: String): StockTrader {
            val trader = StockTrader(username)

            val account = StockTradingAccount(BigDecimal.ZERO, trader)

            trader.tradingAccounts.add(account)

            return trader
        }
    }
}

