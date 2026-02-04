package com.epsilon.nagginggnome.infra.persistence.jpa.user

import com.epsilon.nagginggnome.domain.user.entity.UserSetting
import com.epsilon.nagginggnome.domain.user.repository.UserSettingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserSettingRepositoryJpaAdapter(
    private val jpa: JpaUserSettingRepository
) : UserSettingRepository {

    override fun findById(userId: UUID): UserSetting? {
        return jpa.findById(userId).orElse(null)
    }

    override fun existsById(userId: UUID): Boolean {
        return jpa.existsById(userId)
    }

    override fun save(userSetting: UserSetting): UserSetting {
        return jpa.save(userSetting)
    }
}
