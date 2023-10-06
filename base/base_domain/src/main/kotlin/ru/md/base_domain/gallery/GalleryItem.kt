package ru.md.base_domain.gallery

import java.time.LocalDateTime

data class GalleryItem(
	val id: Long = 0,
	val folderId: Long = 0,
	val name: String = "",
	val description: String? = null,
	val originUrl: String? = null,
	val originKey: String? = null,
	val normalUrl: String? = null,
	val normalKey: String? = null,
	val miniUrl: String? = null,
	val miniKey: String? = null,
	val createdAt: LocalDateTime? = null,
	val updatedAt: LocalDateTime? = null,
)