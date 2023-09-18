package ru.md.base_rest.model.response

import ru.md.base_domain.image.model.ImageType

data class BaseImageResponse(
	val id: Long? = null,
	val imageUrl: String = "",
	val imageKey: String = "",
	val miniUrl: String? = null,
	val miniKey: String? = null,
	val type: ImageType = ImageType.UNDEF,
	val main: Boolean = false,
	val createdAt: Long? = null,
)
