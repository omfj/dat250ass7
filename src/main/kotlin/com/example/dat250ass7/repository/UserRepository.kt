package com.example.dat250ass7.repository

import com.example.dat250ass7.model.entities.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<UserEntity, String> {
    fun findByEmail(email: String): UserEntity?
}
