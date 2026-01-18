package com.epsilon.nagginggnome.domain.user.repository

import com.epsilon.nagginggnome.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID>
