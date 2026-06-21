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
@Table(name = "trading_accounts")
class TradingAccount protected constructor(

    @Column(nullable = false)
    val saxoAccountKey: String,

    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "trader_id", nullable = false, unique = true)
    val trader: Trader,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var _id: Long? = null,

    ) {
    val id: Long
        get() = requireNotNull(_id) {
            "Cannot access id of a TradingAccount that has not been persisted"
        }

    companion object {
        fun createWithTrader(
            username: String,
            saxoClientKey: String,
            saxoAccountKey: String
        ): TradingAccount {
            val trader = Trader.createForAccount(username, saxoClientKey)

            return TradingAccount(saxoAccountKey, trader)
        }
    }
}