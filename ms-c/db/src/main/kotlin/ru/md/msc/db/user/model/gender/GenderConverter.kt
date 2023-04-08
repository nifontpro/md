package ru.md.msc.db.user.model.gender

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.msc.domain.user.model.Gender

@Converter(autoApply = true)
class GenderConverter : AttributeConverter<Gender, String?> {

	override fun convertToDatabaseColumn(gender: Gender): String? {
		return if (gender == Gender.UNDEF) null else gender.code
	}

	override fun convertToEntityAttribute(code: String?): Gender {
		return if (code == null) {
			Gender.UNDEF
		} else {
			Gender.values().find {
				it.code == code
			} ?: Gender.UNDEF
		}
	}
}