package ru.md.msc.rest

import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("user")
class UserController {

	@PostMapping("test")
	suspend fun test() = RS("Test user endpoint: OK")

	@PostMapping("data")
	suspend fun getData(
		@RequestBody body: RS? = null,
		principal: Principal,
	): RS {
		println(principal.name)
		return RS(res = "User data valid, body: ${body?.res}")
	}
}

data class RS(
	val res: String
)