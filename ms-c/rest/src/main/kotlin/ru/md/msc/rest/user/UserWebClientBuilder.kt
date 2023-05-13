package ru.md.msc.rest.user

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

const val resourceServerURL = "http://localhost:8765/client"

@Component
class UserWebClientBuilder(
//	@Value("\${resource-server.url}")
//	private val resourceServerURL: String,
) {

	val userClient = WebClient.create(resourceServerURL)

	// https://www.baeldung.com/kotlin/spring-boot-kotlin-coroutines
	suspend fun getUserData(uri: String, body: Any, accessToken: String): Any {

		return userClient
			.post()
			.uri(uri)
			.bodyValue(body)
			.headers {
				it.set("Authorization", accessToken)
//				it.addAll(HttpHeaders().apply {
//					setBearerAuth(accessToken)
//				})
			}
			.retrieve()
			.awaitBody()
	}

}