package ru.md.base_domain.image.model

data class FileData(
	val entityId: Long = 0, // id сущности, например сотрудника
	val imageUrl: String = "", // нормализованное изображение
	val miniUrl: String = "", // мини
	val originUrl: String = "", // оригинальное
	val filename: String = "",
	val fileExtension: String = "",
	val description: String? = null,
	val size: Long = 0,
	val system: Boolean = false, // Признак системного изображения
	val compress: Boolean = true, // Сжималось ли изображение
	val normCompress: Boolean = true, // Сжималось ли изображение до нормального размера
)