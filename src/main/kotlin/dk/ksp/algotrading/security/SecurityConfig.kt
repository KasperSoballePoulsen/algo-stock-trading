package dk.ksp.algotrading.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val traderDetailsService: TraderDetailsService,
) {

    @Bean
    fun authenticationProvider(passwordEncoder: PasswordEncoder): AuthenticationProvider =
        DaoAuthenticationProvider(traderDetailsService).apply {
            setPasswordEncoder(passwordEncoder)
        }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(12)
}