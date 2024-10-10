package com.example.dat250ass7.model.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
@JsonDeserialize
data class VoteOption(
    val id: Long,
    val caption: String,
    val order: Int,
)
