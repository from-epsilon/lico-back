package com.epsilon.nagginggnome.global.security.crypto

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.Mac
import javax.crypto.SecretKey

/**
 * 토큰 해싱 컴포넌트
 */
@Component
class TokenHasher(
    private val props: TokenHashProperties
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.pepper))

    /**
     * 입력 토큰을 HMAC-SHA256으로 해싱하여 Base64 문자열로 반환합니다.
     */
    fun hash(token: String): String {
        val mac = Mac.getInstance(CryptoConstants.HMAC_SHA256)
        mac.init(key)
        val digest = mac.doFinal(token.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(digest)
    }
}
