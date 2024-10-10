package com.example.dat250ass7.model.input

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
@JsonDeserialize
data class AddVoteInput(
    val optionId: Long,
)
