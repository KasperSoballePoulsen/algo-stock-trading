package dk.ksp.algotrading.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "stock_traders")
class StockTrader(

    @Column(nullable = false, unique = true,)
    val username: String,

    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "trading_account_id", nullable = false, unique = true)
    val tradingAccount: StockTradingAccount,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var deletedAt: Instant? = null
)

