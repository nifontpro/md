package ru.md.msgal.db

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(
	basePackages = [
		"ru.md.msgal",
		"ru.md.base_rest",
		"ru.md.base_s3"
	]
)
class GalleryApplication

fun main(args: Array<String>) {
	runApplication<GalleryApplication>(*args)
}
