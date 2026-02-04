package com.epsilon.nagginggnome.infra.persistence.jpa.push

import com.epsilon.nagginggnome.domain.push.entity.FcmToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaFcmTokenRepository : JpaRepository<FcmToken, UUID> {
    fun findByUserId(userId: UUID): FcmToken?
    fun findByToken(token: String): FcmToken?
    fun deleteByUserId(userId: UUID): Long
    fun deleteByToken(token: String): Long
}
