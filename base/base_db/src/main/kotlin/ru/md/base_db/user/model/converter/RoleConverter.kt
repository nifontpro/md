package ru.md.base_db.user.model.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.base_domain.user.model.RoleUser

@Converter(autoApply = true)
class RoleConverter : AttributeConverter<RoleUser, String> {

	override fun convertToDatabaseColumn(roleUser: RoleUser): String {
		return roleUser.code
	}

	override fun convertToEntityAttribute(code: String): RoleUser {
		return RoleUser.values().find {
			it.code == code
		} ?: throw Exception("RoleMapper: Не найдено значение роли: $code")
	}
}
