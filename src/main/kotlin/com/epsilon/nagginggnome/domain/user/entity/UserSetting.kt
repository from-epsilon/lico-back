package com.epsilon.nagginggnome.domain.user.entity

import jakarta.persistence.*
import java.util.*

/**
 * 유저 설정 정보 엔티티
 */
@Entity
@Table(
    name = "user_settings"
)
class UserSetting(
    user: User,
    nickname: String? = null,
    verbosityPerDay: Double? = null,
    sleepTime: Int? = null,
    wakeTime: Int? = null
) {

    /**
     * PK = user_id
     * - users.id와 동일한 값을 사용함(shared PK)
     * - @MapsId로 인해 user를 세팅하면 이 값이 함께 결정됨
     */
    @Id
    @Column(name = "user_id", nullable = false, updatable = false, columnDefinition = "uuid")
    var userId: UUID? = null
        private set

    /**
     * 1:1 연관관계 주인(owning side)
     * - @JoinColumn(user_id)가 FK 컬럼
     * - @MapsId로 User PK를 UserSetting PK로 공유
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    var user: User = user
        private set

    /**
     * 별명
     */
    @Column(name = "nickname")
    var nickname: String? = nickname
        private set

    /**
     * 하루 알림 강도
     */
    @Column(name = "verbosity_per_day")
    var verbosityPerDay: Double? = verbosityPerDay
        private set

    /**
     * 취침 시간
     */
    @Column(name = "sleep_time")
    var sleepTime: Int? = sleepTime
        private set

    /**
     * 기상 시간
     */
    @Column(name = "wake_time")
    var wakeTime: Int? = wakeTime
        private set

    fun patch(
        nickname: String? = null,
        verbosityPerDay: Double? = null,
        sleepTime: Int? = null,
        wakeTime: Int? = null
    ) {
        nickname?.let { this.nickname = it }
        verbosityPerDay?.let { this.verbosityPerDay = it }
        sleepTime?.let { this.sleepTime = it }
        wakeTime?.let { this.wakeTime = it }
    }
}
