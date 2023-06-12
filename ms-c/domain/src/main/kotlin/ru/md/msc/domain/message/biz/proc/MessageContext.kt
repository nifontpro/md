package ru.md.msc.domain.message.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.message.model.UserMsg
import ru.md.msc.domain.message.service.MessageService

class MessageContext : BaseClientContext() {

	var userMsg: UserMsg = UserMsg()
	var messages: List<UserMsg> = emptyList()
	var messageId: Long = 0
	var readState: Boolean = true

	lateinit var messageService: MessageService
}

enum class MessageCommand : IBaseCommand {

	SEND,
	DELETE,
	GET_AUTH_USER,
//	GET_BY_ID,
	SET_READ_STATUS

}
