package ru.md.msgal.domain.item.model

import java.time.LocalDateTime

class Item(
	val id: Long = 0,
	val folderId: Long = 0,
	val name: String = "",
	val description: String? = null,
	val createdAt: LocalDateTime? = null,
	val updatedAt: LocalDateTime? = null,
	val imageUrl: String = "",
	val imageKey: String = "",
)