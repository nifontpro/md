package ru.md.msc.domain.base.validate.db

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.errorUnauthorized
import ru.md.msc.domain.base.helper.fail

fun <T : BaseContext> ICorChainDsl<T>.getAuthUserAndVerifyEmail(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		if (authId < 1) {
			notValidAuthId()
			return@handle
		}

		authUser = try {
			userService.findById(authId)
		} catch (e: Exception) {
			fail(
				errorDb(
					repository = "user",
					violationCode = "find by id ",
					description = "Ошибка получения сотрудника"
				)
			)
			return@handle
		} ?: run {
			notValidAuthId()
			return@handle
		}

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

private fun <T : BaseContext> T.notValidAuthId() {
	fail(
		errorUnauthorized(
			message = "Неверный authId",
		)
	)
}
