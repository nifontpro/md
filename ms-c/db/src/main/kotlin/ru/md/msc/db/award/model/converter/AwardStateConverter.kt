package ru.md.msc.db.award.model.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.msc.domain.award.model.AwardState

@Converter(autoApply = true)
class AwardStateConverter : AttributeConverter<AwardState, String> {

	override fun convertToDatabaseColumn(deptType: AwardState): String = deptType.code

	override fun convertToEntityAttribute(code: String): AwardState {
		return AwardState.entries.find {
			it.code == code
		} ?: AwardState.ERROR
	}
}