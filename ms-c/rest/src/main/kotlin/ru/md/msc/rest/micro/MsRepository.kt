package ru.md.msc.rest.micro

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Repository
class MsRepository(
	private val microWebClientBuilder: MicroWebClientBuilder,

	@Value("\${keycloak.url}") private val keycloakUrl: String,
	@Value("\${keycloak.credentials.secret}") private val clientSecret: String,
) {

	private val keycloakClient = WebClient.create(keycloakUrl)

	suspend fun token(): AuthResponse {

		// параметры запроса
		val mapForm: MultiValueMap<String, String> = LinkedMultiValueMap()
		mapForm.add("grant_type", "client_credentials")
		mapForm.add("client_id", "msm")
		mapForm.add("client_secret", clientSecret)
		mapForm.add("scope", "profile")

		val authResponse = postKeycloakRequest(uri = "/token", body = mapForm)
		log.info(authResponse.toString())
		return authResponse
	}

	suspend fun postKeycloakRequest(uri: String, body: Any): AuthResponse {
		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
		return keycloakClient
			.post()
			.uri(uri)
			.bodyValue(body)
			.headers { it.addAll(headers) }
			.retrieve()
			.awaitBody()
	}

	companion object {
		val log: Logger = LoggerFactory.getLogger(MsRepository::class.java)
	}

}

@RestController
@RequestMapping("t")
class TestController(
	private val msRepository: MsRepository
) {

	@PostMapping("auth")
	suspend fun auth(): AuthResponse {
		return msRepository.token()
	}

}