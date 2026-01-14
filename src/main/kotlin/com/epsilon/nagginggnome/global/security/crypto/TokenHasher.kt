package com.epsilon.nagginggnome.global.security.crypto

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.SecretKey

@Component
class TokenHasher(
    private val props: TokenHashProperties
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.pepper))

    fun hash(token: String): String {
        val mac = Mac.getInstance(CryptoConstants.HMAC_SHA256)
        mac.init(key)
        val digest = mac.doFinal(token.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(digest)
    }
}
