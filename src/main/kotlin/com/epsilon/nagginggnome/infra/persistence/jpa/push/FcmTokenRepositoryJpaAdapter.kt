package com.epsilon.nagginggnome.infra.persistence.jpa.push

import com.epsilon.nagginggnome.domain.push.entity.FcmToken
import com.epsilon.nagginggnome.domain.push.repository.FcmTokenRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class FcmTokenRepositoryJpaAdapter(
    private val jpa: JpaFcmTokenRepository
) : FcmTokenRepository {

    override fun findByUserId(userId: UUID): FcmToken? = jpa.findByUserId(userId)

    override fun findByToken(token: String): FcmToken? = jpa.findByToken(token)

    override fun save(entity: FcmToken): FcmToken = jpa.save(entity)

    override fun delete(entity: FcmToken) {
        jpa.delete(entity)
    }

    override fun deleteByUserId(userId: UUID): Long = jpa.deleteByUserId(userId)

    override fun deleteByToken(token: String): Long = jpa.deleteByToken(token)
}
