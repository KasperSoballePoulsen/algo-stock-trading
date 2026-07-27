package dk.ksp.algotrading.repository

import dk.ksp.algotrading.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String) : User?
}