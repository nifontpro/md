package ru.md.base_domain.client

import org.springframework.core.ParameterizedTypeReference
import ru.md.base_domain.model.BaseResponse

interface MicroClient {
	suspend fun getDataFromMs(uri: String, requestBody: Any): BaseResponse<String>
	suspend fun <R> postRequest(
		uri: String,
		body: Any,
		type: ParameterizedTypeReference<BaseResponse<R>>
	): BaseResponse<R>?
}