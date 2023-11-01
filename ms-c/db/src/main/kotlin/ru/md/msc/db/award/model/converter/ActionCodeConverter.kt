package ru.md.msc.db.award.model.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.msc.domain.award.model.ActionType

@Converter(autoApply = true)
class ActionCodeConverter : AttributeConverter<ActionType, String> {

	override fun convertToDatabaseColumn(actionType: ActionType): String = actionType.code

	override fun convertToEntityAttribute(code: String): ActionType {
		return ActionType.entries.find {
			it.code == code
		} ?: ActionType.UNDEF
	}
}