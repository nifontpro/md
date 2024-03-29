package ru.md.msc.db

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(
	basePackages = [
		"ru.md.msc",
		"ru.md.base_db",
		"ru.md.base_rest",
		"ru.md.base_client",
		"ru.md.base_s3"
	]
)
class ClientApplication

fun main(args: Array<String>) {
	runApplication<ClientApplication>(*args)
}
