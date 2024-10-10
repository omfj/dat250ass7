package com.example.dat250ass7.repository

import com.example.dat250ass7.model.entities.VoteOptionEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VoteOptionRepository : CrudRepository<VoteOptionEntity, Long>
