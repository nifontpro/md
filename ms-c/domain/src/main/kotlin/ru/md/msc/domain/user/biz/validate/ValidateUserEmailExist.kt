package ru.md.msc.domain.user.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.validateUserEmailExist(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {
		val email = user.authEmail
		if (email.isNullOrBlank()) return@handle
		val deptId = authUser.dept?.id ?: return@handle
		val emailExist = userService.validateEmail(email = email, deptId = deptId)
		if (emailExist) {
			fail(
				errorValidation(
					field = "email",
					violationCode = "user exist",
					description = "Сотрудник с почтой $email уже зарегистрирован в компании"
				)
			)
		}
	}
}
