package ru.md.msc.rest

import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("user")
class UserController {

	@PostMapping("test")
	suspend fun test(): RS {
		val usedMb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576
		return RS("Test user endpoint: OK, used: $usedMb Mb")
	}

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