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

		if (deptId < 1) {
			fail(
				errorUnauthorized(
					message = "Неверный id отдела",
				)
			)
			return@handle
		}

		if (authUser.dept?.id == deptId) return@handle

		val auth = checkRepositoryData {
			deptService.validateDeptLevel(upId = authUser.dept?.id ?: 0, downId = deptId)
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
