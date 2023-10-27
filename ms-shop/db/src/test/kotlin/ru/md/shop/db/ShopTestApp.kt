package ru.md.shop.db

import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main(args: Array<String>) {
	fromApplication<ShopApplication>().with(TestBeans::class).run(*args)
}


