package com.example.dat250ass7.model.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
@JsonDeserialize
data class Poll(
    val id: Long,
    val question: String,
    val ownerId: String,
    val publishedAt: String,
    val expiresAt: String,
    val options: List<VoteOption>,
    val votes: List<Vote> = emptyList(),
)
