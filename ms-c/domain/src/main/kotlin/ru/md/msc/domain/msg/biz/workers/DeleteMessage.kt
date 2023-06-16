package ru.md.msc.domain.msg.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.msg.biz.proc.MessageContext

fun ICorChainDsl<MessageContext>.deleteMessage(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		messageService.deleteById(messageId = messageId)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "message",
				violationCode = "delete",
				description = "Ошибка удаления сообщения"
			)
		)
	}
}