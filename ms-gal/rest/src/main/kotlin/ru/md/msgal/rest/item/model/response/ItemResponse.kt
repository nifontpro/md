package ru.md.msgal.rest.item.model.response

data class ItemResponse(
	val id: Long = 0,
	val folderId: Long = 0,
	val name: String = "",
	val description: String? = null,
	val originUrl: String? = null,
	val originKey: String? = null,
	val imageUrl: String? = null,
	val imageKey: String? = null,
	val miniUrl: String? = null,
	val miniKey: String? = null,
	val createdAt: Long? = null,
	val updatedAt: Long? = null,
)