package ru.md.msc.domain.msg.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.msg.biz.proc.MessageContext
import ru.md.msc.domain.msg.model.MessageType
import java.time.LocalDateTime

fun ICorChainDsl<MessageContext>.prepareSendMessageToUser(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userMsg = userMsg.copy(
			fromId = authUser.id,
			toId = userId,
			type = MessageType.USER,
			msg = userMsg.msg?.trim(),
			sendDate = LocalDateTime.now()
		)
	}
}