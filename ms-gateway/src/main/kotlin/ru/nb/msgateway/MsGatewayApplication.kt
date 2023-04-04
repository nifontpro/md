package ru.nb.msgateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MsmGatewayApplication

fun main(args: Array<String>) {
	runApplication<MsmGatewayApplication>(*args)
}
