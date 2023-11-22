package ru.md.base_domain.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.core.ParameterizedTypeReference
import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.helper.otherError
import ru.md.base_domain.biz.proc.BaseContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.model.BaseResponse

/**
 * Типизированная функция получения данных из другого микросервиса
 */
suspend inline fun <reified T : Any, reified R> BaseContext.getDataFromMs(
	uri: String,
	request: T
): R? {

	val result = microClient.getDataFromMs(uri = uri, requestBody = request)
	if (!result.success) {
		errors.addAll(result.errors)
		state = ContextState.FAILING
		return null
	} else {
		return try {
			jacksonObjectMapper().readValue(result.data, R::class.java)
		} catch (e: Exception) {
			println(e.message)
			fail(
				otherError(
					description = "Ошибка приведения типа при получении данных из микросервиса",
					field = uri,
					level = ContextError.Levels.ERROR
				)
			)
			return null
		}
	}
}

// https://stackoverflow.com/questions/53378161/webflux-webclient-and-generic-types

/**
 * Типизированная функция получения данных из другого микросервиса
 */
suspend inline fun <reified T : Any, reified R> BaseContext.getData(
	uri: String,
	request: T
): R? {

	val typeReference: ParameterizedTypeReference<BaseResponse<R>> =
		object : ParameterizedTypeReference<BaseResponse<R>>() {}

	val result = withContext(Dispatchers.IO) {
		msClient
			.post()
			.uri(uri)
			.bodyValue(request)
			.retrieve()
//		.awaitBody<BaseResponse<R>>()
			.bodyToMono(typeReference)
			.block()
	} ?: return null

	return if (!result.success) {
		errors.addAll(result.errors)
		state = ContextState.FAILING
		null
	} else {
		result.data
	}
}
