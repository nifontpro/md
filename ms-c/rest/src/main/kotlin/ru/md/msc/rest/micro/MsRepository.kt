package ru.md.msc.rest.micro

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("t")
class TestController(
	private val microClient: MicroClient,
) {

	@PostMapping("test")
	suspend fun auth(): Any {
		val body = RS(res = "test")
		return microClient.getDataFromMs(uri = "/gallery/micro/str", requestBody = body)
	}

}

data class RS(
	val res: String
)