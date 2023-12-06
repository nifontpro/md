package ru.md.base_db.user.model.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.base_domain.user.model.Gender

@Converter(autoApply = true)
class GenderConverter : AttributeConverter<Gender, String?> {

	override fun convertToDatabaseColumn(gender: Gender): String? {
		return if (gender == Gender.UNDEF) null else gender.code
	}

	override fun convertToEntityAttribute(code: String?) = code.toGender()
}

fun String?.toGender() = if (this == null) {
	Gender.UNDEF
} else {
	Gender.entries.find {
		it.code == this
	} ?: Gender.UNDEF
}