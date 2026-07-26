package dk.ksp.algotrading.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val traderDetailsService: TraderDetailsService,
) {


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf {
                disable()
            }

            authorizeHttpRequests {
                authorize(HttpMethod.GET, "/api/saxo/oauth/callback", permitAll)
                authorize(anyRequest, authenticated)
            }

            httpBasic { }

            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
        }

        return http.build()
    }


    @Bean
    fun authenticationProvider(passwordEncoder: PasswordEncoder): AuthenticationProvider =
        DaoAuthenticationProvider(traderDetailsService).apply {
            setPasswordEncoder(passwordEncoder)
        }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(12)
}