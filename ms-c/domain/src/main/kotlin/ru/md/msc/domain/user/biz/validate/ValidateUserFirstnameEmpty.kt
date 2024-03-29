package ru.md.msc.domain.user.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.validateUserFirstnameEmpty(title: String) = worker {
	this.title = title
	on { user.firstname.isBlank() }
	handle {
		fail(
			validateUserFirstnameBlankExt()
		)
	}
}

fun validateUserFirstnameBlankExt() = errorValidation(
	field = "firstname",
	violationCode = "blank",
	description = "Поле Имя должно быть заполнено"
)
