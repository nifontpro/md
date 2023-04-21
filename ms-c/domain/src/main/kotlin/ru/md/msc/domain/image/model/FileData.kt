package ru.md.msc.domain.image.model

data class FileData(
	val entityId: Long = 0, // id сущности, например сотрудника
	val url: String = "",
	val filename: String = "",
	val description: String? = null,
	val size: Long = 0,
	val system: Boolean = false, // Признак системного изображения
)