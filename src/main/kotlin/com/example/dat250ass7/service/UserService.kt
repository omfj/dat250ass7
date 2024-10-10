package com.example.dat250ass7.service

import com.example.dat250ass7.model.domain.User
import com.example.dat250ass7.model.domain.UserWithoutPassword
import com.example.dat250ass7.model.domain.withoutPassword
import com.example.dat250ass7.model.entities.UserEntity
import com.example.dat250ass7.model.entities.toUser
import com.example.dat250ass7.model.input.RegisterInput
import com.example.dat250ass7.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun createUser(user: RegisterInput): UserEntity {
        val userEntity = UserEntity()
        userEntity.email = user.email
        userEntity.name = user.name
        userEntity.password = user.password

        userRepository.save(userEntity)

        return userEntity
    }

    fun getUsers(): List<UserWithoutPassword> {
        return userRepository.findAll().map { it.toUser().withoutPassword() }
    }

    fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)?.toUser()
    }
}
