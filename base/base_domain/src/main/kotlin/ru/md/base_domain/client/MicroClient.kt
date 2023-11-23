package ru.md.base_domain.client

import org.springframework.core.ParameterizedTypeReference
import ru.md.base_domain.model.BaseResponse

interface MicroClient {
	suspend fun <R> getDataFromMs(
		uri: String,
		requestBody: Any,
		responseType: ParameterizedTypeReference<BaseResponse<R>>
	): BaseResponse<R>
}