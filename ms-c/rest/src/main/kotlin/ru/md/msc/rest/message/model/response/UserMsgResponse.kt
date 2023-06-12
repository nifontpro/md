package ru.md.msc.rest.message.model.response

import ru.md.msc.domain.message.model.MessageType

data class UserMsgResponse(
	val id: Long = 0,
	val fromId: Long? = null,
	val toId: Long = 0,
	val type: MessageType = MessageType.NONE,
	val msg: String? = null,
	val read: Boolean = false,
	val sendDate: Long = 0,
	val imageUrl: String? = null,
)