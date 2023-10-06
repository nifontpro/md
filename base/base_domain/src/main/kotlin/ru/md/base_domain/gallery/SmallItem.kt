package ru.md.base_domain.gallery

data class SmallItem(
	val id: Long = 0,
	val folderId: Long = 0,
	val name: String = "",
	val originUrl: String? = null,
	val originKey: String? = null,
	val normalUrl: String? = null,
	val normalKey: String? = null,
	val miniUrl: String? = null,
	val miniKey: String? = null,
)