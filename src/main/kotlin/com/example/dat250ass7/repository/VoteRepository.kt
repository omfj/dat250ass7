package com.example.dat250ass7.repository

import com.example.dat250ass7.model.entities.VoteEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VoteRepository : CrudRepository<VoteEntity, Long> {
    @Query("SELECT v FROM VoteEntity v WHERE v.user.id = :userId AND v.poll.id = :pollId")
    fun findByUserIdAndPollId(
        userId: String,
        pollId: Long,
    ): VoteEntity?
}
