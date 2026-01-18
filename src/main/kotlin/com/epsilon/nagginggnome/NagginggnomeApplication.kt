package com.epsilon.nagginggnome

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaAuditing
class NagginggnomeApplication

fun main(args: Array<String>) {
    runApplication<NagginggnomeApplication>(*args)
}
