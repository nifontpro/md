package ru.md.msc.domain.base.validate.auth

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext

/**
 * Проверка доступа авторизованного пользователя к сотруднику
 */
fun <T : BaseClientContext> ICorChainDsl<T>.validateAuthUserLevel(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		if (authUser.id == userId) return@handle

		val auth = deptService.validateUserLevel(upId = authUser.dept?.id ?: 0, userId = userId)

		if (!auth) {
			fail(
				errorUnauthorized(
					role = "userId",
					message = "Доступ к данным сотрудника запрещен",
				)
			)
		}
	}

	except {
		fail(
			errorDb(
				repository = "user",
				violationCode = "user level",
				description = "Ошибка проверки доступа к данным сотрудника"
			)
		)
	}
}
