package ru.md.msgal.rest.item.model.response

data class ItemResponse(
	val id: Long = 0,
	val folderId: Long = 0,
	val name: String = "",
	val description: String? = null,
	val imageUrl: String = "",
	val imageKey: String = "",
	val miniUrl: String = "",
	val miniKey: String = "",
	val createdAt: Long? = null,
	val updatedAt: Long? = null,
)