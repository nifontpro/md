package ru.md.msc.domain.dept.biz.validate.db

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorUnauthorized
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.base.model.checkRepositoryData
import ru.md.msc.domain.dept.biz.proc.DeptContext

/**
 * Проверка, имеет ли администратор доступ к заданному отделу
 */
fun ICorChainDsl<DeptContext>.validateAuthDeptLevel(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		// Если новый отдел создается в отделе создающего Администратора
		if (authUser.dept?.id == dept.parentId) return@handle

		val auth = checkRepositoryData {
			deptService.validateDeptLevel(authUser.dept?.id ?: 0, dept.parentId)
		} ?: return@handle

		if (!auth) {
			fail(
				errorUnauthorized(
					message = "Нет доступа к отделу",
				)
			)
		}
	}
}
