package ru.md.msgal.rest.test

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("micro")
class TesController(
	private val userClient: UserWebClientBuilder
) {

	@PostMapping("str")
	fun getTestStr(
		@RequestBody rs: RS
	) : RS = RS("Test string, ret: ${rs.res}")

//	@PostMapping("t1")
//	suspend fun t1(
//		@RequestHeader(name = AUTH) bearerToken: String,
//		@RequestBody body: RS,
//	): Any {
//		println(bearerToken)
//		return userClient.getUserData(uri = "/user/d", body = body, accessToken = bearerToken)
//	}

}

data class RS(
	val res: String
)