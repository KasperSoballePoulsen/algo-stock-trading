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

@Entity
@Table(name = "stock_trading_accounts")
class StockTradingAccount(

    @Column(nullable = false)
    var cashBalance: BigDecimal,

    @OneToMany(cascade = [CascadeType.PERSIST], mappedBy = "tradingAccount")
    var holdings: MutableList<StockHolding> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.PERSIST])
    var orders: MutableList<StockOrder> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

)