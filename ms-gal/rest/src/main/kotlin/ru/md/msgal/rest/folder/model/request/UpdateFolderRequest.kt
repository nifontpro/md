package ru.md.msgal.rest.folder.model.request

data class UpdateFolderRequest(
	val folderId: Long = 0,
	val name: String = "",
	val description: String? = null,
)