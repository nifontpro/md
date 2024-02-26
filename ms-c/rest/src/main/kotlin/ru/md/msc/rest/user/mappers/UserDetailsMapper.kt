package ru.md.msc.rest.user.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.rest.user.model.response.UserDetailsResponse

fun UserDetails.toUserDetailsResponse(): UserDetailsResponse {
	return UserDetailsResponse(
		user = user.toUserResponse(),
		phone = phone,
		address = address,
		description = description,
		schedule = schedule,
		createdAt = createdAt?.toEpochMilliUTC(),
		birthDate = birthDate?.toEpochMilliUTC(),
		jobDate = jobDate?.toEpochMilliUTC(),
		images = images.map { it.toBaseImageResponse() },
//				images = images.map(BaseImage::toBaseImageResponse)
	)
}