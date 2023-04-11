package ru.md.msc.domain.user.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.base.dom.helper.errorValidation
import ru.md.base.dom.helper.fail
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.validateUserFirstnameEmpty(title: String) = worker {
	this.title = title
	on { user.firstname.isBlank() }
	handle {
		fail(
			errorValidation(
				field = "firstname",
				violationCode = "empty",
				description = "Имя не должно быть пустым"
			)
		)
	}
}
