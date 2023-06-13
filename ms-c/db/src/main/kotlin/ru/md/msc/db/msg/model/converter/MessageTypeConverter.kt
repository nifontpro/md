package ru.md.msc.db.msg.model.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.md.msc.domain.message.model.MessageType

@Converter(autoApply = true)
class MessageTypeConverter : AttributeConverter<MessageType, String> {

	override fun convertToDatabaseColumn(messageType: MessageType): String {
		return messageType.code
	}

	override fun convertToEntityAttribute(code: String): MessageType {
		return MessageType.values().find {
			it.code == code
		} ?: throw Exception("MessageTypeMapper: Не найдено значение типа сообщения: $code")
	}
}
