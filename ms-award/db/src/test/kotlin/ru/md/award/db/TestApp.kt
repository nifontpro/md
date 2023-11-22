package ru.md.medal.db

import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main(args: Array<String>) {
	fromApplication<MedalApplication>().with(TestBeans::class).run(*args)
}
