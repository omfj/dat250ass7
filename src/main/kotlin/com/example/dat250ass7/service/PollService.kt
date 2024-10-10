package com.example.dat250ass7.service

import com.example.dat250ass7.exception.PollNotFoundException
import com.example.dat250ass7.model.domain.Poll
import com.example.dat250ass7.model.entities.PollEntity
import com.example.dat250ass7.model.entities.VoteEntity
import com.example.dat250ass7.model.entities.VoteOptionEntity
import com.example.dat250ass7.model.entities.toPoll
import com.example.dat250ass7.model.input.AddPollInput
import com.example.dat250ass7.repository.PollRepository
import com.example.dat250ass7.repository.UserRepository
import com.example.dat250ass7.repository.VoteOptionRepository
import com.example.dat250ass7.repository.VoteRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Service
class PollService(
    private val pollRepository: PollRepository,
    private val userRepository: UserRepository,
    private val voteRepository: VoteRepository,
    private val voteOptionRepository: VoteOptionRepository,
) {
    fun addPoll(
        newPoll: AddPollInput,
        ownerId: String,
    ) {
        val user = userRepository.findById(ownerId).getOrNull() ?: throw PollNotFoundException(ownerId)

        val poll = PollEntity()
        poll.question = newPoll.question
        poll.expiresAt = LocalDateTime.parse(newPoll.expiresAt)
        poll.publishedAt = LocalDateTime.now()
        poll.owner = user

        pollRepository.save(poll)

        user.polls.add(poll)

        userRepository.save(user)

        newPoll.options.forEach {
            val option = VoteOptionEntity()
            option.poll = poll
            option.caption = it.caption
            option.orderNum = it.order

            voteOptionRepository.save(option)

            poll.options.add(option)
        }

        pollRepository.save(poll)
    }

    fun getPolls(): List<Poll> {
        return pollRepository.findAll().map { it.toPoll() }
    }

    fun getPoll(
        id: Long,
        checkExpiration: Boolean = false,
    ): Poll {
        val poll = pollRepository.findById(id).getOrNull() ?: throw PollNotFoundException(id.toString())

        val expiresAt = LocalDateTime.parse(poll.expiresAt.toString())
        if (checkExpiration && LocalDateTime.now().isAfter(expiresAt)) {
            throw PollNotFoundException(id.toString())
        }

        return poll.toPoll()
    }

    fun deletePoll(id: Long) {
        pollRepository.deleteById(id)
    }

    @Transactional
    fun addVote(
        pollId: Long,
        optionId: Long,
        userId: String,
    ) {
        val poll = pollRepository.findById(pollId).getOrNull() ?: throw PollNotFoundException(pollId.toString())
        val user = userRepository.findById(userId).getOrNull() ?: throw PollNotFoundException(userId)
        val option = voteOptionRepository.findById(optionId).getOrNull() ?: throw PollNotFoundException(optionId.toString())

        val vote = VoteEntity()
        vote.user = user
        vote.poll = poll
        vote.option = option

        voteRepository.save(vote)

        user.votes.add(vote)
        option.votes.add(vote)

        userRepository.save(user)
        voteOptionRepository.save(option)
    }

    fun deleteVote(
        pollId: Long,
        userId: String,
    ) {
        val poll = pollRepository.findById(pollId).getOrNull() ?: throw PollNotFoundException(pollId.toString())
        val user = userRepository.findById(userId).getOrNull() ?: throw PollNotFoundException(userId)
        val vote = voteRepository.findByUserIdAndPollId(userId, pollId) ?: throw PollNotFoundException("Vote")

        user.votes.remove(vote)

        userRepository.save(user)
        pollRepository.save(poll)

        voteRepository.delete(vote)
    }
}
