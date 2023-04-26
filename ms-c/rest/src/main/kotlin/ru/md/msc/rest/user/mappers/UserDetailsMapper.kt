package ru.md.msc.rest.user.mappers

import ru.md.msc.domain.base.model.converter.toEpochMilliUTC
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.rest.user.model.response.UserDetailsResponse

fun UserDetails.toUserDetailsResponse() = UserDetailsResponse(
	user = user,
	phone = phone,
	address = address,
	description = description,
	createdAt = createdAt?.toEpochMilliUTC()
)