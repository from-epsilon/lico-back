package com.epsilon.nagginggnome.domain.user.repository

import com.epsilon.nagginggnome.domain.user.entity.UserSetting
import java.util.UUID

interface UserSettingRepository {
    fun findById(userId: UUID): UserSetting?
    fun existsById(userId: UUID): Boolean
    fun save(userSetting: UserSetting): UserSetting
}
