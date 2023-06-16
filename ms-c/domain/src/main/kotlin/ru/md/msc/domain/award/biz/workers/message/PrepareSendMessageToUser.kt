package ru.md.msc.domain.award.biz.workers.message

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.actionMessage
import ru.md.msc.domain.msg.model.MessageType
import java.time.LocalDateTime

fun ICorChainDsl<AwardContext>.prepareSendActionMessageToUser(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		val msg = buildString {
			append(actionType.actionMessage())
			append(" \"${award.name}\"!")
		}

		userMsg = userMsg.copy(
			fromId = authUser.id,
			toId = userId,
			type = MessageType.SYSTEM,
			msg = msg,
			sendDate = LocalDateTime.now(),
			imageUrl = award.mainImg
		)
	}
}