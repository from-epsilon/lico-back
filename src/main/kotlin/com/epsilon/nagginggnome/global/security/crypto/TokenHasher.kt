package com.epsilon.nagginggnome.global.security.crypto

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.Base64
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
     * 입력 토큰을 HMAC-SHA256으로 해싱하여 Base64 문자열로 반환
     */
    fun hash(token: String): String {
        val digestBytes = hmacSha256(token)
        return Base64.getEncoder().encodeToString(digestBytes)
    }

    /**
     * 입력 토큰(token)이 저장된 토큰 해시(tokenHash)와 일치하는지 검증합니다.
     */
    fun match(token: String, tokenHash: String): Boolean {
        val actualBytes = hmacSha256(token)
        val expectedBytes = Base64.getDecoder().decode(tokenHash)
        return MessageDigest.isEqual(actualBytes, expectedBytes)
    }

    /**
     * token에 대해 HMAC-SHA256 바이트를 계산
     */
    private fun hmacSha256(token: String): ByteArray {
        val mac = Mac.getInstance(CryptoConstants.HMAC_SHA256)
        mac.init(key)
        return mac.doFinal(token.toByteArray(Charsets.UTF_8))
    }
}
