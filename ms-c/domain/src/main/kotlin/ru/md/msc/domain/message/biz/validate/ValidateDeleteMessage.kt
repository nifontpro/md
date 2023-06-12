package ru.md.msc.domain.message.biz.validate

import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.message.biz.proc.MessageContext

fun ICorChainDsl<MessageContext>.validateDeleteMessage(title: String) = worker {
	this.title = title
	on { !(authId == userMsg.toId || authId == userMsg.fromId) }
	handle {
		fail(
			errorUnauthorized(
				role = "msg",
				message = "Нет доступа к сообщению сотрудника",
			)
		)
	}
}
