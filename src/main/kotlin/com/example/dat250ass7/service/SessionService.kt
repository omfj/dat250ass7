package com.example.dat250ass7.service

import com.example.dat250ass7.model.domain.UserWithoutPassword
import com.example.dat250ass7.model.domain.withoutPassword
import com.example.dat250ass7.model.entities.SessionEntity
import com.example.dat250ass7.model.entities.toUser
import com.example.dat250ass7.repository.SessionRepository
import com.example.dat250ass7.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Service
class SessionService(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
) {
    fun validateSessionAndGetUser(sessionId: String): UserWithoutPassword? {
        val session = sessionRepository.findById(sessionId).getOrNull()
        if (session == null) {
            return null
        }

        if (LocalDateTime.now().isAfter(session.expiresAt)) {
            sessionRepository.delete(session)
            return null
        }

        return session.user.toUser().withoutPassword()
    }

    fun createSession(userId: String): SessionEntity? {
        val user = userRepository.findById(userId).getOrNull() ?: return null

        val session = SessionEntity()
        session.expiresAt = LocalDateTime.now().plusDays(7)
        session.user = user

        sessionRepository.save(session)

        user.sessions.add(session)

        userRepository.save(user)

        return session
    }
}
