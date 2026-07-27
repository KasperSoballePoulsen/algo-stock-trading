package dk.ksp.algotrading.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "saxo_clients")
class SaxoClient(

    @OneToOne
    val user: User,

    @Column(nullable = false, unique = true)
    val clientKey: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: Long? = null
)