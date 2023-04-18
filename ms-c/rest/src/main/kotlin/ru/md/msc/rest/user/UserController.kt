package ru.md.msc.rest.user

import org.springframework.web.bind.annotation.*
import ru.md.msc.domain.user.biz.proc.UserProcessor
import ru.md.msc.domain.user.model.User
import ru.md.msc.rest.base.AUTH
import ru.md.msc.rest.base.BaseResponse
import ru.md.msc.rest.base.process
import ru.md.msc.rest.user.mappers.fromTransport
import ru.md.msc.rest.user.mappers.toTransportGetUserDetails
import ru.md.msc.rest.user.mappers.toTransportGetUsers
import ru.md.msc.rest.user.model.request.*
import ru.md.msc.rest.user.model.response.UserDetailsResponse
import ru.md.msc.rest.utils.JwtUtils
import java.security.Principal

@RestController
@RequestMapping("user")
class UserController(
	private val userProcessor: UserProcessor,
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

	@PostMapping("profiles")
	private suspend fun getProfiles(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetProfilesRequest
	): BaseResponse<List<User>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = userProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportGetUsers() }
		)
	}

	@PostMapping("get_by_dept")
	private suspend fun getProfiles(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUsersByDeptRequest
	): BaseResponse<List<User>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = userProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportGetUsers() }
		)
	}

	@PostMapping("delete")
	private suspend fun deleteUser(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteUserRequest
	): BaseResponse<UserDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = userProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportGetUserDetails() }
		)
	}

	@PostMapping("get_id")
	private suspend fun getUserById(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUserByIdRequest
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

}

data class RS(
	val res: String
)