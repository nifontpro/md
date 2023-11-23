package ru.md.base_domain.client

import org.springframework.core.ParameterizedTypeReference
import ru.md.base_domain.biz.proc.BaseContext
import ru.md.base_domain.model.BaseResponse

/**
 * Типизированная функция получения данных из другого микросервиса
 */
suspend inline fun <reified T : Any, reified R> BaseContext.getDataFromMs(
	uri: String,
	request: T
): BaseResponse<R> {

	val responseType: ParameterizedTypeReference<BaseResponse<R>> =
		object : ParameterizedTypeReference<BaseResponse<R>>() {}

	return microClient.getDataFromMs(
		uri = uri,
		requestBody = request,
		responseType = responseType
	)

}
