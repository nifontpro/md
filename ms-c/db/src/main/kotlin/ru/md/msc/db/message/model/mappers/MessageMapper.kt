package ru.md.msc.db.message.model.mappers

import ru.md.msc.db.message.model.UserMsgEntity
import ru.md.msc.domain.message.model.UserMsg

fun UserMsgEntity.toUserMsg() = UserMsg(
	id = id ?: 0,
	fromId = fromId,
	toId = toId,
	type = type,
	msg = msg,
	read = read,
	sendDate = sendDate,
	imageUrl = imageUrl
)

fun UserMsg.toUserMsgEntity(create: Boolean = false) = UserMsgEntity(
	id = if (create) null else id,
	fromId = fromId,
	toId = toId,
	type = type,
	msg = msg,
	read = read,
	sendDate = sendDate,
	imageUrl = imageUrl
)