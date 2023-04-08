package ru.md.msc.db.user.model.mappers

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.msc.domain.user.model.RoleEnum

@Converter(autoApply = true)
class RoleConverter : AttributeConverter<RoleEnum, String> {

	override fun convertToDatabaseColumn(roleEnum: RoleEnum): String {
		return roleEnum.code
	}

	override fun convertToEntityAttribute(code: String): RoleEnum {
		return RoleEnum.values().find {
			it.code == code
		} ?: throw Exception("RoleMapper: Не найдено значение роли: $code")
	}
}
