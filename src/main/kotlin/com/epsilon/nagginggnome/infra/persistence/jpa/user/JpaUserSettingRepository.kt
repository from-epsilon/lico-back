package com.epsilon.nagginggnome.infra.persistence.jpa.user

import com.epsilon.nagginggnome.domain.user.entity.UserSetting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaUserSettingRepository : JpaRepository<UserSetting, UUID>
