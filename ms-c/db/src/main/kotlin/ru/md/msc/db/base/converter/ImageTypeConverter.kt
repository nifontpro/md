package ru.md.msc.db.base.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.msc.domain.image.model.ImageType

@Converter(autoApply = true)
class ImageTypeConverter : AttributeConverter<ImageType, String> {

	override fun convertToDatabaseColumn(roleUser: ImageType): String {
		return roleUser.code
	}

	override fun convertToEntityAttribute(code: String): ImageType {
		return ImageType.values().find {
			it.code == code
		} ?: throw Exception("ImageType: Не найдено значение: $code")
	}
}
