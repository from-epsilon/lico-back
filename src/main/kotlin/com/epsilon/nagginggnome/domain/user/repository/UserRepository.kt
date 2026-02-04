package com.epsilon.nagginggnome.domain.user.repository

import com.epsilon.nagginggnome.domain.user.entity.User
import java.util.UUID

interface UserRepository {
    fun findById(userId: UUID): User?
    fun save(user: User): User
}
