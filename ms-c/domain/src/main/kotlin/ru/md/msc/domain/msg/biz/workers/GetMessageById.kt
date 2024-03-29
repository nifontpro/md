package ru.md.msc.domain.msg.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.msg.biz.proc.MessageContext
import ru.md.msc.domain.msg.biz.proc.MessageNotFoundException
import ru.md.msc.domain.msg.biz.proc.getMessageError
import ru.md.msc.domain.msg.biz.proc.messageNotFoundError

fun ICorChainDsl<MessageContext>.getMessageById(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userMsg = messageService.getById(messageId = messageId)
	}

	except {
		log.error(it.message)
		when (it) {
			is MessageNotFoundException -> messageNotFoundError()
			else -> getMessageError()
		}

	}
}