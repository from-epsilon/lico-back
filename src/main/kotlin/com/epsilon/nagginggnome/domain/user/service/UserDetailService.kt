package com.epsilon.nagginggnome.domain.user.service

import com.epsilon.nagginggnome.domain.user.dto.request.UserDetailCreateRequest
import com.epsilon.nagginggnome.domain.user.dto.request.UserDetailUpdateRequest
import com.epsilon.nagginggnome.domain.user.dto.response.UserDetailGetResponse
import com.epsilon.nagginggnome.domain.user.entity.UserDetail
import com.epsilon.nagginggnome.domain.user.repository.UserDetailRepository
import com.epsilon.nagginggnome.domain.user.repository.UserRepository
import com.epsilon.nagginggnome.global.constant.code.UserErrorCode
import com.epsilon.nagginggnome.global.exception.ApiException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserDetailService(
    private val userRepository: UserRepository,
    private val userDetailRepository: UserDetailRepository
) {

    /**
     * 유저 상세 정보 조회
     */
    @Transactional(readOnly = true)
    fun getUserDetail(userId: UUID): UserDetailGetResponse {
        val userDetail = userDetailRepository.findByIdOrNull(userId)
            ?: throw ApiException(UserErrorCode.USER_DETAIL_NOT_FOUND)

        return UserDetailGetResponse(
            nickname = userDetail.nickname,
            coreValue = userDetail.coreValue,
            motive = userDetail.motive,
            selfImage = userDetail.selfImage,
            verbosityPerDay = userDetail.verbosityPerDay,
            sleepTime = userDetail.sleepTime,
            wakeTime = userDetail.wakeTime
        )
    }

    /**
     * 유저 상세 정보 생성
     *
     * - 이미 detail이 있으면 409(USER_DETAIL_ALREADY_EXISTS)
     */
    @Transactional
    fun createUserDetail(userId: UUID, req: UserDetailCreateRequest) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw ApiException(UserErrorCode.USER_NOT_FOUND)

        if (userDetailRepository.existsById(userId)) {
            throw ApiException(UserErrorCode.USER_DETAIL_ALREADY_EXISTS)
        }

        try {
            userDetailRepository.save(
                UserDetail(
                    user = user,
                    nickname = req.nickname,
                    coreValue = req.coreValue,
                    motive = req.motive,
                    selfImage = req.selfImage,
                    verbosityPerDay = req.verbosityPerDay,
                    sleepTime = req.sleepTime,
                    wakeTime = req.wakeTime
                )
            )
        } catch (_: DataIntegrityViolationException) {
            // 레이스 컨디션으로 PK 중복이 발생할 수 있으므로 409로 매핑
            throw ApiException(UserErrorCode.USER_DETAIL_ALREADY_EXISTS)
        }
    }

    /**
     * 유저 상세 정보 수정
     */
    @Transactional
    fun updateUserDetail(userId: UUID, req: UserDetailUpdateRequest) {
        val userDetail = userDetailRepository.findByIdOrNull(userId)
            ?: throw ApiException(UserErrorCode.USER_DETAIL_NOT_FOUND)

        userDetail.update(
            nickname = req.nickname,
            coreValue = req.coreValue,
            motive = req.motive,
            selfImage = req.selfImage,
            verbosityPerDay = req.verbosityPerDay,
            sleepTime = req.sleepTime,
            wakeTime = req.wakeTime
        )
    }
}
