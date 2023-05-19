package ru.md.base_domain.client

import ru.md.base_domain.rest.BaseResponse

interface MicroClient {
	suspend fun getDataFromMs(uri: String, requestBody: Any): BaseResponse<String>
}