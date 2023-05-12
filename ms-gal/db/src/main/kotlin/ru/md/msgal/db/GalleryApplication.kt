package ru.md.msgal.db

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["ru.md.msgal"])
class ClientApplication

fun main(args: Array<String>) {
	runApplication<ClientApplication>(*args)
}
