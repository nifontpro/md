package ru.md.msc.rest.message.mappers

import ru.md.msc.domain.message.biz.proc.MessageCommand
import ru.md.msc.domain.message.biz.proc.MessageContext
import ru.md.msc.domain.message.model.UserMsg
import ru.md.msc.rest.message.model.request.DeleteMessageRequest
import ru.md.msc.rest.message.model.request.GetAuthUserMessageRequest
import ru.md.msc.rest.message.model.request.SendMessageRequest
import ru.md.msc.rest.message.model.request.SetMessageReadStatusRequest

fun MessageContext.fromTransport(request: SendMessageRequest) {
	command = MessageCommand.SEND
	authId = request.authId
	userId = request.userId
	userMsg = UserMsg(
		toId = userId,
		msg = request.msg
	)
}

fun MessageContext.fromTransport(request: DeleteMessageRequest) {
	command = MessageCommand.DELETE
	authId = request.authId
	messageId = request.messageId
}

fun MessageContext.fromTransport(request: GetAuthUserMessageRequest) {
	command = MessageCommand.GET_AUTH_USER
	authId = request.authId
}

fun MessageContext.fromTransport(request: SetMessageReadStatusRequest) {
	command = MessageCommand.SET_READ_STATUS
	authId = request.authId
	messageId = request.messageId
	readState = request.read
}