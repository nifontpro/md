package ru.md.msc.rest.user.mappers

import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.rest.user.model.response.UserDetailsResponse
import java.time.LocalDateTime
import java.time.ZoneId

fun UserDetails.toUserDetailsResponse() = UserDetailsResponse(
	user = user,
	phone = phone,
	address = address,
	description = description,
	createdAt = createdAt?.toEpochMilli()
)

fun LocalDateTime.toEpochMilli() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()