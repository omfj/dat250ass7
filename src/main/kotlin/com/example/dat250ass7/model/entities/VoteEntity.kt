package com.example.dat250ass7.model.entities

import com.example.dat250ass7.model.domain.Vote
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class VoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    lateinit var user: UserEntity

    @ManyToOne
    lateinit var poll: PollEntity

    @ManyToOne
    lateinit var option: VoteOptionEntity
}

fun VoteEntity.toVote(): Vote {
    return Vote(
        id = id!!,
        userId = user.id,
    )
}
