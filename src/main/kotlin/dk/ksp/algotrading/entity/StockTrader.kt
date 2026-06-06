package dk.ksp.algotrading.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "stock_traders")
class StockTrader(

    @Column(unique = true, nullable = false)
    val username: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToMany(
        mappedBy = "trader",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val portfolio: MutableList<StockHolding> = mutableListOf(),

    var deletedAt: Instant? = null
)

