package ru.md.msc.domain.message.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.message.biz.proc.MessageContext
import ru.md.msc.domain.message.model.MessageType
import java.time.LocalDateTime

fun ICorChainDsl<MessageContext>.sendMessage(title: String) = worker {

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
		messageService.send(userMsg = userMsg)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "message",
				violationCode = "send",
				description = "Ошибка отправки сообщения"
			)
		)
	}
}