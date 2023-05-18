package ru.md.msc.rest.micro

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody

@Component
class MicroClient(
	@Value("\${resource-server.url}") private val resourceServerURL: String,
	@Value("\${keycloak.url}") private val keycloakUrl: String,
	@Value("\${keycloak.credentials.secret}") private val clientSecret: String,
	@Value("\${micro-client.id}") private val microClientId: String,
) {

	val msClient: WebClient = WebClient.create(resourceServerURL)
	private val keycloakClient = WebClient.create(keycloakUrl)

	val mutex = Mutex()
	var token: String? = null

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
			getMsData(uri = uri, body = requestBody, accessToken = accessToken)
		} catch (e: WebClientResponseException) {
			log.error(e.message)
			if (e.statusCode == HttpStatus.UNAUTHORIZED) {
				log.info("Получаем <accessToken>")
				val authResponse = getAccessToken()
				token = authResponse.accessToken
				log.info("Повторный запрос")
				getMsData(uri = uri, body = requestBody, accessToken = authResponse.accessToken)
			} else {
				log.error("Ошибка получения данных из мс, статус: ${e.statusCode}")
				throw e
			}
		}
	}

	suspend fun getMsData(uri: String, body: Any, accessToken: String): Any {
		return msClient
			.post()
			.uri(uri)
			.bodyValue(body)
			.headers {
//				it.set("Authorization", accessToken) // Если с Bearer в строке
				it.addAll(HttpHeaders().apply {
					setBearerAuth(accessToken)
				})
			}
			.retrieve()
			.awaitBody()
	}

	suspend fun getAccessToken(): AuthResponse {
		log.info("Запрос <accessToken>")
		val mapForm: MultiValueMap<String, String> = LinkedMultiValueMap()
		mapForm.add("grant_type", "client_credentials")
		mapForm.add("client_id", microClientId)
		mapForm.add("client_secret", clientSecret)
		mapForm.add("scope", "profile")

		return postKeycloakRequest(uri = "/token", body = mapForm)
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
		val log: Logger = LoggerFactory.getLogger(MicroClient::class.java)
	}

}