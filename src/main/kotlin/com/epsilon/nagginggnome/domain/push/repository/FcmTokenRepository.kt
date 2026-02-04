package com.epsilon.nagginggnome.domain.push.repository

import com.epsilon.nagginggnome.domain.push.entity.FcmToken
import java.util.UUID

interface FcmTokenRepository {
    fun findByUserId(userId: UUID): FcmToken?
    fun findByToken(token: String): FcmToken?
    fun save(entity: FcmToken): FcmToken
    fun delete(entity: FcmToken)
    fun deleteByUserId(userId: UUID): Long
    fun deleteByToken(token: String): Long
}
