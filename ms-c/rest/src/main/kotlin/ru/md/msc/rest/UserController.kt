package ru.md.msc.rest

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController {

	@PostMapping("test")
	suspend fun test() = RS("Test user endpoint: OK")
}

data class RS(
	val res: String
)