package ru.md.msc.domain.message.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.message.biz.proc.MessageContext

fun ICorChainDsl<MessageContext>.validateMessageId(title: String) = worker {
	this.title = title
	on { messageId < 1 }
	handle {
		fail(
			errorValidation(
				field = "messageId",
				violationCode = "not valid",
				description = "Неверный id сообщения"
			)
		)
	}
}
