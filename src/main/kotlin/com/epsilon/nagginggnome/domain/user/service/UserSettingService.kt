package com.epsilon.nagginggnome.domain.user.service

import com.epsilon.nagginggnome.domain.user.dto.request.UserSettingCreateRequest
import com.epsilon.nagginggnome.domain.user.dto.request.UserSettingUpdateRequest
import com.epsilon.nagginggnome.domain.user.dto.response.UserSettingCreateResponse
import com.epsilon.nagginggnome.domain.user.dto.response.UserSettingGetResponse
import com.epsilon.nagginggnome.domain.user.dto.response.UserSettingUpdateResponse
import com.epsilon.nagginggnome.domain.user.entity.UserSetting
import com.epsilon.nagginggnome.domain.user.repository.UserRepository
import com.epsilon.nagginggnome.domain.user.repository.UserSettingRepository
import com.epsilon.nagginggnome.global.constant.code.UserErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserSettingService(
    private val userRepository: UserRepository,
    private val userSettingRepository: UserSettingRepository
) {

    /**
     * 유저 설정 정보 조회
     */
    @Transactional(readOnly = true)
    fun getUserSetting(userId: UUID): UserSettingGetResponse {
        val userSetting = userSettingRepository.findByIdOrNull(userId)
            ?: throw ApiException(UserErrorCode.USER_SETTING_NOT_FOUND)

        return UserSettingGetResponse(
            nickname = userSetting.nickname,
            verbosityPerDay = userSetting.verbosityPerDay,
            sleepTime = userSetting.sleepTime,
            wakeTime = userSetting.wakeTime
        )
    }

    /**
     * 유저 설정 정보 생성
     *
     * - 이미 setting이 있으면 409(USER_SETTING_ALREADY_EXISTS)
     */
    @Transactional
    fun createUserSetting(userId: UUID, req: UserSettingCreateRequest): UserSettingCreateResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw ApiException(UserErrorCode.USER_NOT_FOUND)

        if (userSettingRepository.existsById(userId)) {
            throw ApiException(UserErrorCode.USER_SETTING_ALREADY_EXISTS)
        }

        val userSetting = try {
            userSettingRepository.save(
                UserSetting(
                    user = user,
                    nickname = req.nickname,
                    verbosityPerDay = req.verbosityPerDay,
                    sleepTime = req.sleepTime,
                    wakeTime = req.wakeTime
                )
            )
        } catch (_: DataIntegrityViolationException) {
            // 동시 요청 레이스 컨디션으로 PK(user_id) 중복이 발생할 수 있으므로 409로 매핑
            throw ApiException(UserErrorCode.USER_SETTING_ALREADY_EXISTS)
        }

        return UserSettingCreateResponse(
            nickname = userSetting.nickname,
            verbosityPerDay = userSetting.verbosityPerDay,
            sleepTime = userSetting.sleepTime,
            wakeTime = userSetting.wakeTime
        )
    }

    /**
     * 유저 설정 정보 수정
     */
    @Transactional
    fun updateUserSetting(userId: UUID, req: UserSettingUpdateRequest): UserSettingUpdateResponse {
        val userSetting = userSettingRepository.findByIdOrNull(userId)
            ?: throw ApiException(UserErrorCode.USER_SETTING_NOT_FOUND)

        userSetting.patch(
            nickname = req.nickname,
            verbosityPerDay = req.verbosityPerDay,
            sleepTime = req.sleepTime,
            wakeTime = req.wakeTime
        )

        return UserSettingUpdateResponse(
            nickname = userSetting.nickname,
            verbosityPerDay = userSetting.verbosityPerDay,
            sleepTime = userSetting.sleepTime,
            wakeTime = userSetting.wakeTime
        )
    }
}
