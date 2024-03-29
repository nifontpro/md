package ru.md.msc.rest.user.model.response

import ru.md.base_rest.model.response.BaseImageResponse

data class UserDetailsResponse(
	val user: UserResponse? = null,
	val phone: String? = null,
	val address: String? = null,
	val description: String? = null,
	val schedule: String? = null,
	val createdAt: Long? = null,
	val birthDate: Long? = null,
	val jobDate: Long? = null,
	val images: List<BaseImageResponse> = emptyList(),
)