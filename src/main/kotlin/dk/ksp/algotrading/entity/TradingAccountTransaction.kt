package dk.ksp.algotrading.entity

import dk.ksp.algotrading.enum.AccountTransactionType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "trading_account_transactions")
class TradingAccountTransaction(

    @Column(nullable = false)
    val amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: AccountTransactionType,

    @Column(nullable = false)
    val balanceAfter: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "trading_account_id", nullable = false)
    val tradingAccount: TradingAccount,

    @OneToOne
    @JoinColumn(name = "order_id")
    val order: Order? = null,

    @Column(nullable = false)
    val timestamp: Instant = Instant.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)