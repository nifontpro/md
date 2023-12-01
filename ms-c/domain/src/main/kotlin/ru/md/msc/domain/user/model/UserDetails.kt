package ru.md.msc.domain.user.model

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.user.model.User
import java.time.LocalDateTime

data class UserDetails(
	val user: User = User(),
	val phone: String? = null,
	val address: String? = null,
	val description: String? = null,
	val schedule: String? = null,
	val createdAt: LocalDateTime? = null,
	var tabId: Long? = null,
	val images: List<BaseImage> = emptyList(),
)