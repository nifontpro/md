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
fun <T : BaseContext> ICorChainDsl<T>.validateAuthDeptLevel(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		if (authDeptId < 1) {
			fail(
				errorUnauthorized(
					message = "Неверный отдел",
				)
			)
			return@handle
		}

		if (authUser.dept?.id == authDeptId) return@handle

		val auth = checkRepositoryData {
			deptService.validateDeptLevel(authUser.dept?.id ?: 0, authDeptId)
		} ?: return@handle

		if (!auth) {
			fail(
				errorUnauthorized(
					role = "deptId",
					message = "Нет доступа к отделу",
				)
			)
		}
	}
}
