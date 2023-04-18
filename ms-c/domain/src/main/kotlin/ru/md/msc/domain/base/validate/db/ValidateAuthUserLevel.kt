package ru.md.msc.domain.base.validate.db

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorUnauthorized
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.base.model.checkRepositoryData

/**
 * Проверка, имеет ли администратор доступ к заданному отделу
 */
fun <T : BaseContext> ICorChainDsl<T>.validateAuthUserLevel(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		if (userId < 1) {
			fail(
				errorUnauthorized(
					message = "Неверный id сотрудника",
				)
			)
			return@handle
		}

		if (authUser.id == userId) return@handle

		val auth = checkRepositoryData {
			deptService.validateUserLevel(upId = authUser.dept?.id ?: 0, userId = userId)
		} ?: return@handle

		if (!auth) {
			fail(
				errorUnauthorized(
					role = "userId",
					message = "Нет доступа к сотруднику",
				)
			)
		}
	}
}
