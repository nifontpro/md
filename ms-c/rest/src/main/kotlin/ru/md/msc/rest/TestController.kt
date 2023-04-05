package ru.md.msc.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

	@GetMapping("test")
	fun test() = "Test rest controller: OK"
}