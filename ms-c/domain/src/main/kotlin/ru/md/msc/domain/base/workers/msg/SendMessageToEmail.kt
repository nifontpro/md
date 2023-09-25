package ru.md.msc.domain.base.workers.msg

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.msg.biz.proc.sendMessageToEmailError

fun <T : BaseClientContext> ICorChainDsl<T>.sendMessageToEmail(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		val email = user.authEmail
		if (email.isNullOrBlank()) return@handle
		emailService.sendMail(toEmail = email, message = userMsg.msg ?: "")
	}

	except {
		log.error(it.message)
		sendMessageToEmailError()
	}
}