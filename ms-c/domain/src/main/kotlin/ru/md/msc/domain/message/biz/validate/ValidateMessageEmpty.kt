package ru.md.msc.domain.message.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.message.biz.proc.MessageContext

fun ICorChainDsl<MessageContext>.validateMessageEmpty(title: String) = worker {
	this.title = title
	on { userMsg.msg.isNullOrBlank() }
	handle {
		fail(
			errorValidation(
				field = "message",
				violationCode = "empty",
				description = "Сообщение не должно быть пустым"
			)
		)
	}
}
