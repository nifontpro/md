package ru.md.base_domain.image.model

import java.time.LocalDateTime

data class BaseImage(
	override val id: Long? = null,
	override val originUrl: String? = null,
	override val originKey: String? = null,
	override val normalUrl: String? = null,
	override val normalKey: String? = null,
	override val miniUrl: String? = null,
	override val miniKey: String? = null,
	override val type: ImageType = ImageType.UNDEF,
	override val main: Boolean = false,
	override val createdAt: LocalDateTime? = null,
) : IBaseImage
