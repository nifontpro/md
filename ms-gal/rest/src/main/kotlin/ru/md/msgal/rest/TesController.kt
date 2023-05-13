package ru.md.msgal.rest

import org.springframework.web.bind.annotation.*

const val AUTH = "Authorization"

@RestController
@RequestMapping("test")
class TesController(
	private val userClient: UserWebClientBuilder
) {

	@PostMapping("t1")
	suspend fun t1(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody body: RS,
	): Any {
		println(bearerToken)
		return userClient.getUserData(uri = "/user/d", body = body, accessToken = bearerToken)
	}

}

data class RS(
	val res: String
)