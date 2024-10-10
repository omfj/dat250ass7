package com.example.dat250ass7.controller

import com.example.dat250ass7.exception.NotSignedInException
import com.example.dat250ass7.exception.PollVoteOptionNotFoundException
import com.example.dat250ass7.exception.UnauthorizedException
import com.example.dat250ass7.model.domain.Poll
import com.example.dat250ass7.model.domain.UserWithoutPassword
import com.example.dat250ass7.model.input.AddPollInput
import com.example.dat250ass7.model.input.AddVoteInput
import com.example.dat250ass7.service.PollService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Poll")
@RestController
@RequestMapping("/api/polls")
class PollController(
    private val pollService: PollService,
) {
    @GetMapping
    fun getPolls(): List<Poll> {
        return pollService.getPolls()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addPoll(
        @RequestBody poll: AddPollInput,
        @AuthenticationPrincipal user: UserWithoutPassword?,
    ): String {
        if (user == null) {
            throw NotSignedInException()
        }

        pollService.addPoll(poll, user.id)

        return "OK"
    }

    @GetMapping("/{id}")
    fun getPoll(
        @PathVariable id: String,
    ): Poll {
        return pollService.getPoll(id.toLong(), true)
    }

    @DeleteMapping("/{id}")
    fun deletePoll(
        @PathVariable id: String,
        @AuthenticationPrincipal user: UserWithoutPassword?,
    ): String {
        if (user == null) {
            throw NotSignedInException()
        }

        val poll = pollService.getPoll(id.toLong())

        if (poll.ownerId != user.id) {
            throw UnauthorizedException()
        }

        pollService.deletePoll(id.toLong())

        return "OK"
    }

    @PostMapping("/{id}/vote")
    @ResponseStatus(HttpStatus.CREATED)
    fun addVote(
        @PathVariable id: String,
        @RequestBody vote: AddVoteInput,
        @AuthenticationPrincipal user: UserWithoutPassword?,
    ): Poll {
        val poll = pollService.getPoll(id.toLong(), checkExpiration = true)

        if (!poll.options.any { it.id == vote.optionId }) {
            throw PollVoteOptionNotFoundException(poll.id.toString(), vote.optionId.toString())
        }

        if (user == null) {
            throw NotSignedInException()
        }

        pollService.addVote(id.toLong(), vote.optionId, user.id)

        return pollService.getPoll(id.toLong())
    }

    @DeleteMapping("/{id}/vote")
    fun deleteVote(
        @PathVariable id: String,
        @RequestBody vote: AddVoteInput,
        @AuthenticationPrincipal user: UserWithoutPassword?,
    ): Poll {
        val poll = pollService.getPoll(id.toLong(), checkExpiration = true)

        if (!poll.options.any { it.id == vote.optionId }) {
            throw PollVoteOptionNotFoundException(poll.id.toString(), vote.optionId.toString())
        }

        if (user == null) {
            throw NotSignedInException()
        }

        pollService.deleteVote(id.toLong(), user.id)

        return pollService.getPoll(id.toLong())
    }
}
