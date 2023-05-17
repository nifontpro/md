package ru.md.msc.rest.micro

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

const val resourceServerURL = "http://localhost:8765"

@Component
class MicroWebClientBuilder(
//	@Value("\${resource-server.url}")
//	private val resourceServerURL: String,
) {

	val msClient: WebClient = WebClient.create(resourceServerURL)

	// https://www.baeldung.com/kotlin/spring-boot-kotlin-coroutines
	suspend fun getMsData(uri: String, body: Any, accessToken: String): Any {

		return msClient
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