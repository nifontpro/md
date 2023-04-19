package ru.md.msc.domain.user.biz.validate.db

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.user.biz.proc.getUserError
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.validateOwnerByEmailExist(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		// Обязательно устанавливаем email!
		user = user.copy(authEmail = authEmail)

		val ownerExist = try {
			userService.doesOwnerWithEmailExist(user.authEmail)
		} catch (e: Exception) {
			getUserError()
			return@handle
		}

		if (ownerExist) fail(
			errorValidation(
				field = "email",
				violationCode = "owner already exist",
				description = "Владелец с почтой ${user.authEmail} уже существует"
			)
		)
	}
}
