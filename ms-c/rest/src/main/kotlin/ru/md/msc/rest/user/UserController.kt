package ru.md.msc.rest.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.*
import ru.md.msc.domain.user.biz.proc.UserProcessor
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.service.UserService
import ru.md.msc.rest.base.AUTH
import ru.md.msc.rest.base.BaseResponse
import ru.md.msc.rest.base.process
import ru.md.msc.rest.user.mappers.fromTransport
import ru.md.msc.rest.user.mappers.toTransportGetUserDetails
import ru.md.msc.rest.user.model.request.CreateOwnerRequest
import ru.md.msc.rest.user.model.response.UserDetailsResponse
import ru.md.msc.rest.utils.JwtUtils
import java.security.Principal

@RestController
@RequestMapping("user")
class UserController(
	private val userProcessor: UserProcessor,
	private val userService: UserService,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("test")
	suspend fun test(): RS {
		val usedMb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576
		return RS("Test user endpoint: OK, used: $usedMb Mb")
	}

	@PostMapping("create_owner")
	private suspend fun createOwner(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: CreateOwnerRequest
	): BaseResponse<UserDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = userProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportGetUserDetails() }
		)
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
		return withContext(Dispatchers.IO) {
			userService.getAll()
		}
	}
}

data class RS(
	val res: String
)