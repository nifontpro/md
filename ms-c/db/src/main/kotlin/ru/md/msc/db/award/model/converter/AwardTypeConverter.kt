package ru.md.msc.db.award.model.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.msc.domain.award.model.AwardType

@Converter(autoApply = true)
class AwardTypeConverter : AttributeConverter<AwardType, String> {

	override fun convertToDatabaseColumn(deptType: AwardType): String = deptType.code

	override fun convertToEntityAttribute(code: String): AwardType {
		return AwardType.values().find {
			it.code == code
		} ?: AwardType.UNDEF
	}
}