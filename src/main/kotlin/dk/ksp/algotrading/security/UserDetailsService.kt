package dk.ksp.algotrading.security

import dk.ksp.algotrading.repository.SaxoTradingAccountRepository
import dk.ksp.algotrading.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException(username)

        return UserDetails(
            id = user.id,
            username = user.username,
            passwordHash = user.passwordHash
        )
    }
}