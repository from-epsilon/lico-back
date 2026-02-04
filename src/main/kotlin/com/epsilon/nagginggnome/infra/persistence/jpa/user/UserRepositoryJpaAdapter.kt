package com.epsilon.nagginggnome.infra.persistence.jpa.user

import com.epsilon.nagginggnome.domain.user.entity.User
import com.epsilon.nagginggnome.domain.user.repository.UserRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserRepositoryJpaAdapter(
    private val jpa: JpaUserRepository
) : UserRepository {

    override fun findById(userId: UUID): User? {
        return jpa.findById(userId).orElse(null)
    }

    override fun save(user: User): User {
        return jpa.save(user)
    }
}
