package com.example.dat250ass7.model.entities

import com.example.dat250ass7.model.domain.Poll
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.LocalDateTime

@Entity
class PollEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    lateinit var question: String

    lateinit var publishedAt: LocalDateTime

    lateinit var expiresAt: LocalDateTime

    @ManyToOne
    lateinit var owner: UserEntity

    @OneToMany(mappedBy = "poll", cascade = [CascadeType.ALL], orphanRemoval = true)
    var options: MutableList<VoteOptionEntity> = mutableListOf()

    @OneToMany(mappedBy = "poll", cascade = [CascadeType.ALL], orphanRemoval = true)
    var votes: MutableList<VoteEntity> = mutableListOf()
}

fun PollEntity.toPoll(): Poll {
    return Poll(
        id = id!!,
        question = question,
        ownerId = owner.id,
        publishedAt = publishedAt.toString(),
        expiresAt = expiresAt.toString(),
        options = options.map { it.toVoteOption() },
        votes = votes.map { it.toVote() },
    )
}
