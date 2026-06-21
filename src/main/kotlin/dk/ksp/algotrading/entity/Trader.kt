package dk.ksp.algotrading.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "traders")
class Trader protected constructor(

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val saxoClientKey: String,

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
        internal fun createForAccount(
            username: String,
            saxoClientKey: String
        ): Trader {
            return Trader(username, saxoClientKey)
        }
    }
}

