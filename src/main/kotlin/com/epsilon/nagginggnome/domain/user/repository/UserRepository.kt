package com.epsilon.nagginggnome.domain.user.repository

import com.epsilon.nagginggnome.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * UserSocialAccount 엔티티 전용 Repository
 */
@Repository
interface UserRepository : JpaRepository<User, UUID>
