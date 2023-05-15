package ru.md.base_domain.image.model

import java.time.LocalDateTime

data class BaseImage(
	override val id: Long? = null,
	override val imageUrl: String = "",
	override val imageKey: String = "",
	override val type: ImageType = ImageType.UNDEF,
	override val main: Boolean = false,
	override val createdAt: LocalDateTime? = null,
) : IBaseImage
