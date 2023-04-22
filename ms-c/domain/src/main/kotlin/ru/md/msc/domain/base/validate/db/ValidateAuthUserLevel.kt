package ru.md.msc.domain.base.validate.db

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.errorUnauthorized
import ru.md.msc.domain.base.helper.fail

/**
 * Проверка доступа администратора к сотруднику
 */
fun <T : BaseContext> ICorChainDsl<T>.validateAuthUserLevel(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		if (authUser.id == userId) return@handle

		val auth = try {
			deptService.validateUserLevel(upId = authUser.dept?.id ?: 0, userId = userId)
		} catch (e: Exception) {
			fail(
				errorDb(
					repository = "user",
					violationCode = "user level",
					description = "Ошибка проверки доступа к данным сотрудника"
				)
			)
			return@handle
		}

		if (!auth) {
			fail(
				errorUnauthorized(
					role = "userId",
					message = "Доступ к данным сотрудника запрещен",
				)
			)
		}
	}
}
