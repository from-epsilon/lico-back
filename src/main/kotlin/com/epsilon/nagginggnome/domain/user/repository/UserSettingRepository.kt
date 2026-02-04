package com.epsilon.nagginggnome.domain.user.repository

import com.epsilon.nagginggnome.domain.user.entity.UserSetting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserSettingRepository : JpaRepository<UserSetting, UUID>
