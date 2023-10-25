package ru.md.base_db.dept.model.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.base_domain.dept.model.DeptType

@Converter(autoApply = true)
class DeptTypeConverter : AttributeConverter<DeptType, String> {

	override fun convertToDatabaseColumn(deptType: DeptType): String = deptType.code

	override fun convertToEntityAttribute(code: String): DeptType {
		return DeptType.values().find {
			it.code == code
		} ?: DeptType.UNDEF
	}
}