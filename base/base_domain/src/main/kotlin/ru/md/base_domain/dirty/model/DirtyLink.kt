package ru.md.base_domain.dirty.model

import java.time.LocalDateTime

data class DirtyLink(
	val id: Long = 0,
	val key: String = "",
	val repo: String = "",
	val saveAs: LocalDateTime = LocalDateTime.now()
)