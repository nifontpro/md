package ru.md.msc.db.user.model.mappers

import ru.md.msc.db.user.model.UserDetailsEntity
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails
import java.time.LocalDateTime

fun UserDetailsEntity.toUserDetails() = UserDetails(
	phone = phone,
	address = address,
	description = description,
	createdAt = createdAt,
	user = user?.toUser() ?: User()
)

fun UserDetails.toUserDetailsEntity(create: Boolean = false) = UserDetailsEntity(
	phone = phone,
	address = address,
	description = description,
	createdAt = if (create) LocalDateTime.now() else createdAt,
	user = user.toUserEntity()
)