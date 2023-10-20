package ru.md.msc.domain.user.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.validateUserLastnameEmpty(title: String) = worker {
	this.title = title
	on { user.lastname?.isBlank() ?: false}
	handle {
		fail(
			validateUserLastnameBlankExt()
		)
	}
}

fun validateUserLastnameBlankExt() = errorValidation(
	field = "lastname",
	violationCode = "blank",
	description = "Поле Фамилия должно быть заполнено"
)
