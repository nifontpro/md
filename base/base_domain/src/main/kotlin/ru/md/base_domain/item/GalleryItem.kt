package ru.md.base_domain.item

import java.time.LocalDateTime

data class GalleryItem(
	val id: Long = 0,
	val folderId: Long = 0,
	val name: String = "",
	val description: String? = null,
	val imageUrl: String = "",
	val imageKey: String = "",
	val createdAt: LocalDateTime? = null,
	val updatedAt: LocalDateTime? = null,
)