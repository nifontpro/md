package ru.md.msgal.rest.folder.model.response

data class FolderResponse(
	val id: Long = 0,
	val parentId: Long = 0,
	val name: String = "",
	val description: String? = null,
	val createdAt: Long? = null,
	val updatedAt: Long? = null,
)