package com.example.dat250ass7.model.entities

import com.example.dat250ass7.model.domain.User
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: String

    lateinit var name: String

    lateinit var email: String

    lateinit var password: String

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var sessions: MutableList<SessionEntity> = mutableListOf()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var votes: MutableList<VoteEntity> = mutableListOf()

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], orphanRemoval = true)
    var polls: MutableList<PollEntity> = mutableListOf()
}

fun UserEntity.toUser(): User {
    return User(
        id = id,
        name = name,
        email = email,
        password = password,
    )
}
