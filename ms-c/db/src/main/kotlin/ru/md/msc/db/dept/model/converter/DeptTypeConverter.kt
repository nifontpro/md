package ru.md.msc.db.dept.model.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.msc.domain.dept.model.DeptType

@Converter(autoApply = true)
class DeptTypeConverter : AttributeConverter<DeptType, String> {

	override fun convertToDatabaseColumn(deptType: DeptType): String {
		return if (deptType == DeptType.UNDEF) "N" else deptType.code
	}

	override fun convertToEntityAttribute(code: String): DeptType {
		return DeptType.values().find {
			it.code == code
		} ?: DeptType.UNDEF
	}
}