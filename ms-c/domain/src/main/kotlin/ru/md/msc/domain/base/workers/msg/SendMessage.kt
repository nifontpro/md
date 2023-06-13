package ru.md.msc.domain.base.workers.msg

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.message.biz.proc.sendMessageError

fun <T : BaseClientContext> ICorChainDsl<T>.sendMessage(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		messageService.send(userMsg = userMsg)
	}

	except {
		log.error(it.message)
		sendMessageError()
	}
}