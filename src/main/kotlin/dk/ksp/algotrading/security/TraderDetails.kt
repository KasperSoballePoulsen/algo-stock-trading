package dk.ksp.algotrading.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class TraderDetails(
    val traderId: Long,
    val tradingAccountId: Long,
    private val traderUsername: String,
    private val traderPasswordHash: String
) : UserDetails {

    override fun getUsername(): String = traderUsername

    override fun getPassword(): String = traderPasswordHash

    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))
}