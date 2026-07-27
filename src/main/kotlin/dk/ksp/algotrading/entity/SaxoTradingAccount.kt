package dk.ksp.algotrading.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "saxo_trading_accounts")
class SaxoTradingAccount(

    @ManyToOne(cascade = [CascadeType.PERSIST])
    val saxoClient: SaxoClient,

    @Column(nullable = false)
    val saxoAccountKey: String,

    @Column(nullable = false, unique = true)
    val saxoAccountId: String,

    @Column(nullable = false)
    var orderHistoryNextPollUrl: String,

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