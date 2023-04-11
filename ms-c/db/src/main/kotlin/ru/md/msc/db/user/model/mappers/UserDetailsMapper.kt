package ru.md.msc.db.user.model.mappers

import ru.md.msc.db.user.model.UserDetailsEntity
import ru.md.msc.domain.user.model.UserDetails
import java.time.LocalDateTime

fun UserDetailsEntity.toUserDetails() = UserDetails(
	phone = phone,
	address = address,
	description = description,
	createdAt = createdAt,
	user = user?.toUser()
)

fun UserDetails.toUserDetailsEntity() = UserDetailsEntity(
	phone = phone,
	address = address,
	description = description,
	createdAt = LocalDateTime.now(),
	user = user?.toUserEntity()
)