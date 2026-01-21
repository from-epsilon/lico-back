package com.epsilon.nagginggnome.domain.user.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "user_details"
)
class UserDetail(
    nickname: String,
    coreValue: String,
    motive: String,
    selfImage: String,
    verbosityPerDay: Double,
    sleepTime: Int,
    wakeTime: Int
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
     * - @MapsId로 User PK를 UserDetail PK로 공유
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null
        private set

    /**
     * 별명
     */
    @Column(name = "nickname", nullable = false)
    var nickname: String = nickname
        private set

    /**
     * 가치관
     */
    @Column(name = "core_value", nullable = false)
    var coreValue: String = coreValue
        private set

    /**
     * 동기
     */
    @Column(name = "motive", nullable = false)
    var motive: String = motive
        private set

    /**
     * 자아상
     */
    @Column(name = "self_image", nullable = false)
    var selfImage: String = selfImage
        private set

    /**
     * 하루 알림 강도
     */
    @Column(name = "verbosity_per_day", nullable = false)
    var verbosityPerDay: Double = verbosityPerDay
        private set

    /**
     * 취침 시간
     */
    @Column(name = "sleep_time", nullable = false)
    var sleepTime: Int = sleepTime
        private set

    /**
     * 기상 시간
     */
    @Column(name = "wake_time", nullable = false)
    var wakeTime: Int = wakeTime
        private set

    /**
     * 양방향 동기화를 위한 내부용 메서드
     * - User.attachDetail에서만 호출되도록 사용하는 편이 안전함
     */
    fun attachUser(user: User) {
        this.user = user
    }
}
