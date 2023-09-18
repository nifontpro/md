package ru.md.msc.db.tc

import org.springframework.boot.fromApplication
import org.springframework.boot.with
import ru.md.msc.db.ClientApplication

fun main(args: Array<String>) {
	fromApplication<ClientApplication>().with(TestBeans::class).run(*args)
}


