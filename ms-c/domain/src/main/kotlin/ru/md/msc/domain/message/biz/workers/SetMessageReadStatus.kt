package ru.md.msc.domain.message.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.message.biz.proc.MessageContext
import ru.md.msc.domain.message.biz.proc.updateMessageError

fun ICorChainDsl<MessageContext>.setMessageReadStatus(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userMsg = messageService.setReadState(messageId = messageId, readState = readState)
	}

	except {
		log.error(it.message)
		updateMessageError()
	}
}