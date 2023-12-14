package ru.md.base_domain.image.model

import java.io.InputStream

data class ImageData(
	val entityId: Long = 0, // id сущности, например сотрудника
	val normalImage: Pair<InputStream, Int>? = null, // нормализованное изображение <is,size>
	val miniImage: Pair<InputStream, Int>? = null, // мини
	val originImage: Pair<InputStream, Int>? = null, // оригинальное
	val imageName: String = "",
	val contentType: String = "",
	val compress: Boolean = true, // Сжималось ли изображение
	val normCompress: Boolean = true, // Сжималось ли изображение до нормального размера
)