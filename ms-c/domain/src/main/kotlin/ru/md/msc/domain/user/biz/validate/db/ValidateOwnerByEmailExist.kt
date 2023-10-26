package ru.md.msc.domain.user.biz.validate.db

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.user.biz.errors.getUserError
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.validateOwnerByEmailExist(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		// Обязательно устанавливаем email!
		user = user.copy(authEmail = authEmail)

		val ownerExist = try {
			userService.doesOwnerWithEmailExist(user.authEmail ?: "")
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
