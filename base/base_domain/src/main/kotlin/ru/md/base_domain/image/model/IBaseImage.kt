package ru.md.base_domain.image.model

import java.time.LocalDateTime

interface IBaseImage {
	val id: Long?
	val originUrl: String?
	val originKey: String?
	val normalUrl: String?
	val normalKey: String?
	val miniUrl: String?
	val miniKey: String?
	val type: ImageType
	val main: Boolean
	val createdAt: LocalDateTime?
}