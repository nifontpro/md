package ru.md.msc.domain.message.biz.validate

import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.message.biz.proc.MessageContext

fun ICorChainDsl<MessageContext>.validateModifyMessage(title: String) = worker {
	this.title = title
	on { authId != userMsg.toId }
	handle {
		fail(
			errorUnauthorized(
				role = "msg",
				message = "Изменить статус прочтения может только сам адресант",
			)
		)
	}
}
