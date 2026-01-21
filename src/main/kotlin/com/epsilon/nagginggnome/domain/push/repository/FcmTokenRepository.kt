package com.epsilon.nagginggnome.domain.push.repository

import com.epsilon.nagginggnome.domain.push.entity.FcmToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FcmTokenRepository : JpaRepository<FcmToken, UUID> {

    fun findByUserId(userId: UUID): FcmToken?

    fun findByToken(token: String): FcmToken?

    fun deleteByUserId(userId: UUID): Long
}
