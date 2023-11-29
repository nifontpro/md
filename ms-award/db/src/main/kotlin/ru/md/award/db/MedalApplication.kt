package ru.md.award.db

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(
	basePackages = [
		"ru.md.award.db",
		"ru.md.base_db",
		"ru.md.base_rest",
		"ru.md.base_client",
		"ru.md.base_s3"
	]
)
class MedalApplication

fun main(args: Array<String>) {
	runApplication<MedalApplication>(*args)
}
