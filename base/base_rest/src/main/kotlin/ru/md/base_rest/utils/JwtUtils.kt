package ru.md.base_rest.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import ru.md.base_rest.model.AuthRequest
import java.util.*
import javax.naming.AuthenticationException

@Component
class JwtUtils {

	val decoder: Base64.Decoder = Base64.getUrlDecoder()
	val mapper = jacksonObjectMapper()

	fun decodeBearerJwt(bearerToken: String): AuthData {
		val token = if (bearerToken.startsWith("Bearer ")) {
			bearerToken.substring(7)
		} else {
			throw AuthenticationException()
		}
		val chunks = token.split(".")
		if (chunks.size != 3) throw AuthenticationException()
		val payload = decoder.decode(chunks[1]).toString(Charsets.UTF_8)
		return mapper.readValue(payload)
	}

	fun <T> baseRequest(request: T, bearerToken: String): AuthRequest<T> {
		val authData = decodeBearerJwt(bearerToken)
		return AuthRequest(
			data = request,
			authEmail = authData.email,
			emailVerified = authData.emailVerified
		)
	}
}