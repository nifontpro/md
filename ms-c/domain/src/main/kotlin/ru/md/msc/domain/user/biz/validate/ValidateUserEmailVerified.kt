package ru.md.msc.domain.user.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.base.dom.helper.errorValidation
import ru.md.base.dom.helper.fail
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.validateUserEmailVerified(title: String) = worker {
	this.title = title
	on { !emailVerified }
	handle {
		fail(
			errorValidation(
				field = "emailVerified",
				violationCode = "false",
				description = "Регистрация невозможна с непроверенной почтой"
			)
		)
	}
}
