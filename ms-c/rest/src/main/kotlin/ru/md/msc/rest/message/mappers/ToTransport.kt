package ru.md.msc.rest.message.mappers

import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.msc.domain.message.biz.proc.MessageContext
import ru.md.msc.rest.message.model.response.UserMsgResponse

fun MessageContext.toTransportUserMsgResponse(): BaseResponse<UserMsgResponse> {
	return baseResponse(userMsg.toUserMsgResponse())
}

fun MessageContext.toTransportUserMessagesResponse(): BaseResponse<List<UserMsgResponse>> {
	return baseResponse(messages.map { it.toUserMsgResponse() })
}