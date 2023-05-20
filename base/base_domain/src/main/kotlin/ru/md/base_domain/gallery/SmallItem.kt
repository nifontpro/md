package ru.md.base_domain.gallery

data class SmallItem(
	val id: Long = 0,
	val folderId: Long = 0,
	val name: String = "",
	val imageUrl: String = "",
	val imageKey: String = "",
)