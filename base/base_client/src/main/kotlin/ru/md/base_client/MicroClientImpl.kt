package ru.md.base_client

import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody
import ru.md.base_domain.client.MicroClient
import ru.md.base_domain.errors.extMsGetATError
import ru.md.base_domain.errors.extMsGetDataError
import ru.md.base_domain.model.BaseResponse

/**
 * Компонент для получения данных из других микросервисов
 * с запросом accessToken из keycloak
 */

@Component
class MicroClientImpl(
	@Value("\${ms.gateway.url}") private val resourceServerURL: String,
	@Value("\${keycloak.url}") private val keycloakUrl: String,
	@Value("\${keycloak.credentials.secret}") private val clientSecret: String,
	@Value("\${micro-client.id}") private val microClientId: String,
) : MicroClient {

	val msClient: WebClient = WebClient.create(resourceServerURL)
	private val keycloakClient = WebClient.create(keycloakUrl)

	val mutex = Mutex()
	var token: String? = null

	override suspend fun <R> getDataFromMs(
		uri: String,
		requestBody: Any,
		responseType: ParameterizedTypeReference<BaseResponse<R>>
	): BaseResponse<R> {
		val accessToken = mutex.withLock {
			token ?: run {
				val newToken = getAccessToken().accessToken
				token = newToken
				newToken
			}
		}

		return try {
			postRequest(
				uri = uri,
				body = requestBody,
				accessToken = accessToken,
				type = responseType
			)
		} catch (e: WebClientResponseException) {
			if (e.statusCode == HttpStatus.UNAUTHORIZED) {
				val authResponse = try {
					getAccessToken()
				} catch (e: Exception) {
					log.error(e.message)
					return BaseResponse.error(errors = listOf(extMsGetATError()))
				}
				token = authResponse.accessToken
				postRequest(
					uri = uri,
					body = requestBody,
					accessToken = accessToken,
					type = responseType
				)
			} else {
				log.error(e.message)
				BaseResponse.error(errors = listOf(extMsGetDataError()))
			}
		} catch (e: Exception) {
			log.error(e.message)
			BaseResponse.error(errors = listOf(extMsGetDataError()))
		}
	}

	// https://stackoverflow.com/questions/53378161/webflux-webclient-and-generic-types
	override suspend fun <R> postRequest(
		uri: String,
		body: Any,
		accessToken: String?,
		type: ParameterizedTypeReference<BaseResponse<R>>
	): BaseResponse<R> {
		return msClient
			.post()
			.uri(uri)
			.bodyValue(body)
			.headers {
//				it.set("Authorization", accessToken) // Если с Bearer в строке
				it.addAll(HttpHeaders().apply {
					accessToken?.let { token -> setBearerAuth(token) }
				})
			}
			.retrieve()
			.bodyToMono(type)
			.awaitSingle()
	}

	suspend fun getAccessToken(): AuthResponse {
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
		val log: Logger = LoggerFactory.getLogger(MicroClientImpl::class.java)
	}

}