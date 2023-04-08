package ru.md.msc.domain.base.model

import java.sql.Timestamp

interface BaseImage {
	val imageUrl: String?
	val imageKey: String?
	val type: ImageType
	val main: Boolean?
	val createdAt: Timestamp?
}