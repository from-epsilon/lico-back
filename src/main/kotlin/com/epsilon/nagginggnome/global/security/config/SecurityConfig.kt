package com.epsilon.nagginggnome.global.security.config

import com.epsilon.nagginggnome.global.security.handler.ApiAccessDeniedHandler
import com.epsilon.nagginggnome.global.security.handler.ApiAuthenticationEntryPoint
import com.epsilon.nagginggnome.global.security.jwt.filter.JwtAuthenticationFilter
import com.epsilon.nagginggnome.global.security.jwt.filter.JwtExceptionFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    val apiAuthenticationEntryPoint: ApiAuthenticationEntryPoint,
    val apiAccessDeniedHandler: ApiAccessDeniedHandler,
    val jwtAuthenticationFilter: JwtAuthenticationFilter,
    val jwtExceptionFilter: JwtExceptionFilter,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                it.requestMatchers(*SecurityPaths.permitAllPatterns()).permitAll()
                it.anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(apiAuthenticationEntryPoint)
                it.accessDeniedHandler(apiAccessDeniedHandler)
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter::class.java)
        return http.build()
    }
}
