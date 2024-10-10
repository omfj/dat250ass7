package com.example.dat250ass7.model.entities

import com.example.dat250ass7.model.domain.VoteOption
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class VoteOptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    lateinit var caption: String

    var orderNum: Int? = null

    @ManyToOne
    lateinit var poll: PollEntity

    @OneToMany(mappedBy = "option", cascade = [CascadeType.ALL], orphanRemoval = true)
    var votes: MutableList<VoteEntity> = mutableListOf()
}

fun VoteOptionEntity.toVoteOption(): VoteOption {
    return VoteOption(
        id = id!!,
        caption = caption,
        order = orderNum!!,
    )
}
