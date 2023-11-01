package ru.md.shop.db.pay.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.shop.domain.pay.model.PayCode

@Converter(autoApply = true)
class PayCodeConverter : AttributeConverter<PayCode, String> {

	override fun convertToDatabaseColumn(actionType: PayCode): String = actionType.code

	override fun convertToEntityAttribute(code: String): PayCode {
		return PayCode.entries.find {
			it.code == code
		} ?: PayCode.UNDEF
	}
}