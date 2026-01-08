package com.epsilon.nagginggnome.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.HandlerTypePredicate
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * 모든 @RestController 엔드포인트 경로 앞에 프리픽스를 자동으로 붙이는 설정 클래스
 */
@Configuration
class ApiVersionPrefixConfig : WebMvcConfigurer {

    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        configurer.addPathPrefix(
            "/v1",
            HandlerTypePredicate.forAnnotation(RestController::class.java)
        )
    }
}
