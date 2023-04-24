package ru.md.msc.domain.dept.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.dept.biz.proc.DeptContext

fun ICorChainDsl<DeptContext>.updateDept(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			deptDetails = deptService.update(deptDetails = deptDetails)
		} catch (e: Exception) {
			log.info(e.message)
			fail(
				errorDb(
					repository = "dept",
					violationCode = "update",
					description = "Ошибка обновления профиля отдела"
				)
			)
		}
	}
}