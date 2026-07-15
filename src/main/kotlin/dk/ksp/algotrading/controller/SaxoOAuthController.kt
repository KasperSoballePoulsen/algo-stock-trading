package dk.ksp.algotrading.controller

import dk.ksp.algotrading.service.SaxoOAuthService
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/saxo/oauth")
class SaxoOAuthController(
    private val saxoOAuthService: SaxoOAuthService
) {

    @GetMapping("/login")
    fun login(
        session: HttpSession,
        response: HttpServletResponse
    ) {
        val state = UUID.randomUUID().toString()

        session.setAttribute(OAUTH_STATE, state)

        response.sendRedirect(
            saxoOAuthService.createAuthorizationUrl(state)
        )
    }

    @GetMapping("/callback")
    fun callback(
        @RequestParam code: String,
        @RequestParam state: String,
        session: HttpSession
    ): ResponseEntity<String> {
        val expectedState =
            session.getAttribute(OAUTH_STATE) as? String

        if (expectedState == null || expectedState != state) {
            return ResponseEntity
                .badRequest()
                .body("Invalid OAuth state")
        }

        session.removeAttribute(OAUTH_STATE)

        saxoOAuthService.handleCallback(code)

        return ResponseEntity.ok(
            "Saxo authorization completed. " +
                    "You can now restart the application."
        )
    }

    companion object {
        private const val OAUTH_STATE = "saxo-oauth-state"
    }
}