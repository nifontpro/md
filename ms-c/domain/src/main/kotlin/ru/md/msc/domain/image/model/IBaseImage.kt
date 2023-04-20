package ru.md.msc.domain.image.model

import java.time.LocalDateTime

interface IBaseImage {
	val id: Long?
	val imageUrl: String
	val imageKey: String
	val type: ImageType
	val main: Boolean
	val createdAt: LocalDateTime?
}