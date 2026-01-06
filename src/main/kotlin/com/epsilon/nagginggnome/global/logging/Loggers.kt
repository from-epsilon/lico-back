package com.epsilon.nagginggnome.global.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 로거 생성을 표준화하기 위한 유틸 함수
 */
inline fun <reified T> logger(): Logger = LoggerFactory.getLogger(T::class.java)
