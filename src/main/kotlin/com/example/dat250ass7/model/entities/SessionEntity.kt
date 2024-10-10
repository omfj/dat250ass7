package com.example.dat250ass7.model.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import java.time.LocalDateTime

@Entity
class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: String

    @Temporal(TemporalType.TIMESTAMP)
    lateinit var expiresAt: LocalDateTime

    @ManyToOne
    lateinit var user: UserEntity
}
