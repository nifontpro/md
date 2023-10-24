package ru.md.shop.db

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["ru.md.shop", "ru.md.base_rest", "ru.md.base_client", "ru.md.base_db"])
class ShopApplication

fun main(args: Array<String>) {
	runApplication<ShopApplication>(*args)
}
