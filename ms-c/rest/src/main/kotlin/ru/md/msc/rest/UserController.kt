package ru.md.msc.rest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.*
import ru.md.msc.domain.user.biz.TestBiz
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.service.UserService
import java.security.Principal

@RestController
@RequestMapping("user")
class UserController(
	private val userService: UserService,
	private val testBiz: TestBiz
) {

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

	@GetMapping("all")
	suspend fun getAll(): List<User> {
		println(testBiz.test())
		return withContext(Dispatchers.IO) {
			userService.getAll()
		}
	}
}

data class RS(
	val res: String
)