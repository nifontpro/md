package ru.md.msc.domain.user.biz.validate.db

import ru.md.base.dom.biz.ContextState
import ru.md.base.dom.helper.errorValidation
import ru.md.base.dom.helper.fail
import ru.md.base.dom.model.checkRepositoryData
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.validateOwnerByEmailExist(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		val ownerExist = checkRepositoryData {
			userService.doesOwnerWithEmailExist(user.email)
		} ?: return@handle

		if (ownerExist) fail(
			errorValidation(
				field = "email",
				violationCode = "owner already exist",
				description = "Владелец с почтой ${user.email} уже существует"
			)
		)
	}
}
