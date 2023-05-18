package ru.md.msc.rest.micro

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody

@Repository
class MsRepository(
	private val microClient: MicroWebClientBuilder,

	@Value("\${keycloak.url}") private val keycloakUrl: String,
	@Value("\${keycloak.credentials.secret}") private val clientSecret: String,
) {

	private val keycloakClient = WebClient.create(keycloakUrl)
	val mutex = Mutex()
	var token: String? = null

	suspend fun testStr(): Any {
		val body = RS(res = "test")
		return getDataFromMs(uri = "/gallery/micro/str", requestBody = body)
	}

	suspend fun getDataFromMs(uri: String, requestBody: Any): Any {
		if (token != null) log.info("<accessToken> найден")
		val accessToken = mutex.withLock {
			token ?: run {
				val tok = getAccessToken().accessToken
				token = tok
				tok
			}
		}

		return try {
			log.info("Получение данных из мс")
			microClient.getMsData(uri = uri, body = requestBody, accessToken = accessToken)
		} catch (e: WebClientResponseException) {
			log.error(e.message)
			if (e.statusCode == HttpStatus.UNAUTHORIZED) {
				log.info("Получаем <accessToken>")
				val authResponse = getAccessToken()
				token = authResponse.accessToken
				log.info("Повторный запрос")
				microClient.getMsData(uri = uri, body = requestBody, accessToken = authResponse.accessToken)
			} else {
				log.error("Ошибка получения данных из мс, статус: ${e.statusCode}")
				throw e
			}
		}
	}

	suspend fun getAccessToken(): AuthResponse {
		log.info("Запрос <accessToken>")
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

	@PostMapping("test")
	suspend fun auth(): Any {
		return msRepository.testStr()
	}

}

data class RS(
	val res: String
)