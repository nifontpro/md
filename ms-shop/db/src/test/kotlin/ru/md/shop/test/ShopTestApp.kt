package ru.md.shop.test

import org.springframework.boot.fromApplication
import org.springframework.boot.with
import ru.md.shop.db.ShopApplication

fun main(args: Array<String>) {
	fromApplication<ShopApplication>().with(TestBeans::class).run(*args)
}


