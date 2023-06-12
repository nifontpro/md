package ru.md.msc.domain.message.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.message.biz.proc.MessageContext
import ru.md.msc.domain.message.biz.proc.getMessageError

fun ICorChainDsl<MessageContext>.getMessageByAuthUser(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		messages = messageService.getByUser(userId = authId)
	}

	except {
		log.error(it.message)
		getMessageError()
	}
}