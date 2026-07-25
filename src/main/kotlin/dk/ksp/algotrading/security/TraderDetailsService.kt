package dk.ksp.algotrading.security

import dk.ksp.algotrading.repository.TradingAccountRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class TraderDetailsService(
    private val tradingAccountRepository: TradingAccountRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val tradingAccount = tradingAccountRepository.findByTraderUsername(username)
            ?: throw UsernameNotFoundException(username)

        return TraderDetails(
            traderId = tradingAccount.trader.id,
            tradingAccountId = tradingAccount.id,
            traderUsername = tradingAccount.trader.username,
            traderPasswordHash = tradingAccount.trader.passwordHash
        )
    }
}