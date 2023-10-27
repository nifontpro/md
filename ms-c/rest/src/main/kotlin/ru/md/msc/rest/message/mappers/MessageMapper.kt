package ru.md.msc.rest.message.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.msc.domain.msg.model.UserMsg
import ru.md.msc.rest.message.model.response.UserMsgResponse

fun UserMsg.toUserMsgResponse() = UserMsgResponse(
	id = id,
	fromId = fromId,
	toId = toId,
	type = type,
	msg = msg,
	read = read,
	sendDate = sendDate.toEpochMilliUTC(),
	imageUrl = imageUrl
)