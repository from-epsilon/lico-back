package com.epsilon.nagginggnome

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class NagginggnomeApplication

fun main(args: Array<String>) {
	runApplication<NagginggnomeApplication>(*args)
}
