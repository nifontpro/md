package ru.md.award.db.medal.model.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.award.domain.medal.model.ActClass

@Converter(autoApply = true)
class ActClassConverter : AttributeConverter<ActClass, String> {

	override fun convertToDatabaseColumn(actClass: ActClass): String = actClass.code

	override fun convertToEntityAttribute(code: String): ActClass {
		return ActClass.entries.find {
			it.code == code
		} ?: ActClass.UNDEF
	}
}