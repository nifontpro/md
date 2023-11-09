package ru.md.msc.rest.user.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.rest.user.model.response.UserDetailsResponse

fun UserDetails.toUserDetailsResponse(): UserDetailsResponse {
	val baseImages = images.map { it.toBaseImageResponse() }
	return UserDetailsResponse(
		user = user.toUserResponse(baseImages),
		phone = phone,
		address = address,
		description = description,
		createdAt = createdAt?.toEpochMilliUTC(),
		images = baseImages
	)
}