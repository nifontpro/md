package ru.md.msc.domain.dept.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.dept.biz.proc.DeptContext

fun ICorChainDsl<DeptContext>.createDept(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		deptDetails = try {
			deptService.create(deptDetails)
		} catch (e: Exception) {
			fail(
				errorDb(
					repository = "dept",
					violationCode = "dept create",
					description = "Ошибка создания отдела"
				)
			)
			return@handle
		}

	}
}