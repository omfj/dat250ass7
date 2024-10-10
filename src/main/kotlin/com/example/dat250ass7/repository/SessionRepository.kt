package com.example.dat250ass7.repository

import com.example.dat250ass7.model.entities.SessionEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionRepository : CrudRepository<SessionEntity, String> {
    fun findSessionById(id: String): SessionEntity?
}
