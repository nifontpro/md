package ru.md.msc.db.user.model.mappers

import ru.md.base_db.image.mappers.toBaseImage
import ru.md.base_db.user.model.mappers.toUser
import ru.md.base_db.user.model.mappers.toUserEntity
import ru.md.msc.db.user.model.UserDetailsEntity
import ru.md.msc.domain.user.model.UserDetails
import java.time.LocalDateTime

fun UserDetailsEntity.toUserDetails() = UserDetails(
	phone = phone,
	address = address,
	description = description,
	createdAt = createdAt,
	user = user.toUser(),
	images = images.map { it.toBaseImage() }
)

fun UserDetails.toUserDetailsEntity(create: Boolean = false) = UserDetailsEntity(
	phone = phone,
	address = address,
	description = description,
	createdAt = if (create) LocalDateTime.now() else createdAt,
	user = user.toUserEntity(create = create)
)