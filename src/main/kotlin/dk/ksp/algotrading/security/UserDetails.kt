package dk.ksp.algotrading.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetails(
    val id: Long,
    private val username: String,
    private val passwordHash: String
) : UserDetails {

    override fun getUsername(): String = username

    override fun getPassword(): String = passwordHash

    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))
}