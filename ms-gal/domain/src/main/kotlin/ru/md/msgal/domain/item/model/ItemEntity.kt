package ru.md.msgal.domain.item.model

import java.time.LocalDateTime

class ItemEntity(
	val id: Long = 0,
	var folderId: Long = 0,
	var name: String = "",
	var description: String? = null,
	var createdAt: LocalDateTime? = null,
	var updatedAt: LocalDateTime? = null,
	var imageUrl: String = "",
	var imageKey: String = "",
)