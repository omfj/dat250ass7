package com.example.dat250ass7.exception

class PollVoteOptionNotFoundException(pollId: String, voteOptionId: String) :
    RuntimeException("Poll with id $pollId does not have vote option with id $voteOptionId")
