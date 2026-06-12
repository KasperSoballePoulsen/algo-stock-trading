package dk.ksp.algotrading.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "traders")
class Trader protected constructor(

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val saxoClientKey: String,

    @OneToMany(mappedBy = "trader", cascade = [CascadeType.PERSIST])
    val tradingAccounts: MutableList<TradingAccount> = mutableListOf(),

    var deletedAt: Instant? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var _id: Long? = null
) {
    val id: Long
        get() = requireNotNull(_id) {
            "Cannot access id of a Trader that has not been persisted"
        }

    companion object {

        fun create(
            username: String,
            saxoClientKey: String,
            saxoAccountKey: String,
            cashAvailableForTrading: BigDecimal
        ): Trader {

            val trader = Trader(username, saxoClientKey)

            val account = TradingAccount(cashAvailableForTrading, saxoAccountKey, trader)

            trader.tradingAccounts.add(account)

            return trader
        }
    }
}

