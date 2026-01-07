package com.epsilon.nagginggnome.domain.user.constant

enum class Role {
    USER,
    ADMIN,
    GUEST;

    fun toAuthority(): String = "ROLE_$name"

    companion object {
        /**
         * 문자열을 Role로 안전 변환
         */
        fun from(value: String?): Role? =
            value
                ?.trim()
                ?.uppercase()
                ?.let { normalized -> entries.firstOrNull { it.name == normalized } }
    }
}
