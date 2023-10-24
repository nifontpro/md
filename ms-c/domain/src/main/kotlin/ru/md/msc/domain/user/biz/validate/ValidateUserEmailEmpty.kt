package ru.md.msc.domain.user.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.validateUserEmailEmpty(title: String) = worker {
	this.title = title
	on { user.authEmail.isNullOrBlank() }
	handle {
		fail(
			validateUserEmailBlankExt()
		)
	}
}

fun validateUserEmailBlankExt() = errorValidation(
	field = "email",
	violationCode = "blank",
	description = "Почта должна быть указана"
)
