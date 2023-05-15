package ru.md.msgal.rest.folder.model.request

data class CreateFolderRequest(
	val parentId: Long = 0,
	val name: String = "",
	val description: String? = null,
)