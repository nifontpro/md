package ru.md.msc.rest.message.mappers

import ru.md.base_domain.model.BaseResponse
import ru.md.base_domain.model.baseResponse
import ru.md.msc.domain.msg.biz.proc.MessageContext
import ru.md.msc.rest.message.model.response.UserMsgResponse

fun MessageContext.toTransportUserMsgResponse(): BaseResponse<UserMsgResponse> {
	return baseResponse(userMsg.toUserMsgResponse())
}

fun MessageContext.toTransportUserMessagesResponse(): BaseResponse<List<UserMsgResponse>> {
	return baseResponse(messages.map { it.toUserMsgResponse() })
}