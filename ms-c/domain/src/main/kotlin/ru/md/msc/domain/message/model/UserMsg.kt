package ru.md.msc.domain.message.model

import java.time.LocalDateTime

data class UserMsg (
	val id: Long = 0,
	val fromId: Long? = null,
	val toId: Long = 0,
	val type: MessageType = MessageType.NONE,
	val msg: String? = null,
	val read: Boolean = false,
	val sendDate: LocalDateTime = LocalDateTime.now(),
	val imageUrl: String? = null,
)

@Suppress("unused")
enum class MessageType(val code: String) {
	NONE("N"),
	SYSTEM("S"),
	USER("U")
}