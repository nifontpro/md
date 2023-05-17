package ru.md.msgal.rest.micro

import org.springframework.web.bind.annotation.*
import ru.md.base_rest.model.AUTH

@RestController
@RequestMapping("micro")
class MicroController {

	@PostMapping("t1")
	suspend fun t1(
		@RequestHeader(name = AUTH) bearerToken: String,
	): String {
		println(bearerToken)
		return "Ok"
	}

}