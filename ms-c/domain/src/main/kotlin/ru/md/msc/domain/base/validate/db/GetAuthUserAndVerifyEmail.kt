package ru.md.msc.domain.base.validate.db

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorUnauthorized
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.base.model.checkRepositoryData

fun <T : BaseContext> ICorChainDsl<T>.getAuthUserAndVerifyEmail(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		if (authId < 1) {
			fail(
				errorUnauthorized(
					message = "Неверный authId",
				)
			)
			return@handle
		}

		authUser = checkRepositoryData {
			userService.findById(authId)
		} ?: return@handle

		log.info("Get Auth user: $authUser")

		if (authUser.authEmail != authEmail) {
			fail(
				errorUnauthorized(
					message = "Доступ по authId запрещен",
				)
			)
			return@handle
		}
	}
}