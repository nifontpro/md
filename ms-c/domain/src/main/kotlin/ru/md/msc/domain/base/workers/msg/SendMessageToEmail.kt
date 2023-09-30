package ru.md.msc.domain.base.workers.msg

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.actionMessageHtml
import ru.md.msc.domain.msg.biz.proc.sendMessageToEmailError

fun ICorChainDsl<AwardContext>.sendMessageToEmail(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		val email = user.authEmail
		if (email.isNullOrBlank()) return@handle
		val msg = actionType.actionMessageHtml(user = user, authUser = authUser, award = award)
		emailService.sendHtml(toEmail = email, message = msg)
	}

	except {
		log.error(it.message)
		sendMessageToEmailError()
	}
}