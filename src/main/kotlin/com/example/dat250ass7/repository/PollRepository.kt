package com.example.dat250ass7.repository

import com.example.dat250ass7.model.entities.PollEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PollRepository : CrudRepository<PollEntity, Long>
