package ru.md.msc.rest.user.model.request

import ru.md.base_rest.model.request.BaseRequest

data class GetUsersWithAwardCountRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val baseRequest: BaseRequest? = null
)