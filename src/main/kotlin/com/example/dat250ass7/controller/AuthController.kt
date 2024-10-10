package com.example.dat250ass7.controller

import com.example.dat250ass7.exception.UserExistsException
import com.example.dat250ass7.model.domain.UserWithoutPassword
import com.example.dat250ass7.model.entities.SessionEntity
import com.example.dat250ass7.model.input.LoginInput
import com.example.dat250ass7.model.input.RegisterInput
import com.example.dat250ass7.service.SessionService
import com.example.dat250ass7.service.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth")
@RestController
class AuthController(
    private val userService: UserService,
    private val sessionService: SessionService,
) {
    @Value("\${session.cookie.name}")
    private lateinit var cookieName: String

    @GetMapping("/api/whoami")
    fun whoami(
        @AuthenticationPrincipal user: UserWithoutPassword?,
    ): UserWithoutPassword? {
        return user
    }

    @PostMapping("/api/logout")
    fun logout(response: HttpServletResponse): String {
        val cookie = Cookie(cookieName, null)
        cookie.maxAge = 0
        response.addCookie(cookie)

        return "OK"
    }

    @PostMapping("/api/login")
    fun login(
        @RequestBody loginInput: LoginInput,
        response: HttpServletResponse,
    ): String {
        val user = userService.getUserByEmail(loginInput.email)

        if (user == null || user.password != loginInput.password) {
            return "Invalid email or password"
        }

        val session = sessionService.createSession(user.id) ?: return "Failed to create session"

        val cookie = createAuthCookie(session)
        response.addCookie(cookie)

        return "OK"
    }

    @PostMapping("/api/register")
    fun register(
        @RequestBody registerInput: RegisterInput,
        response: HttpServletResponse,
    ): String {
        val existingUser = userService.getUserByEmail(registerInput.email)
        if (existingUser != null) {
            throw UserExistsException()
        }

        val user = userService.createUser(registerInput)

        val session = sessionService.createSession(user.id) ?: throw RuntimeException("Failed to create session")

        val cookie = createAuthCookie(session)
        response.addCookie(cookie)

        return "OK"
    }

    private fun createAuthCookie(session: SessionEntity): Cookie {
        val cookie = Cookie(cookieName, session.id)
        cookie.maxAge = session.expiresAt.nano / 1000000
        cookie.secure = false // SHOULD BE CHANGED IN PRODUCTION
        cookie.isHttpOnly = true
        cookie.path = "/"

        return cookie
    }
}
