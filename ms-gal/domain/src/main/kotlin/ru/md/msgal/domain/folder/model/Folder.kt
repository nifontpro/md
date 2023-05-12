package ru.md.msgal.domain.folder.model

import java.time.LocalDateTime

data class Folder(
	val id: Long = 0,
	val parentId: Long = 0,
	val name: String = "",
	val description: String? = null,
	val createdAt: LocalDateTime? = null,
	val updatedAt: LocalDateTime? = null,
)