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
		deptDetails = deptService.update(deptDetails = deptDetails)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "dept",
				violationCode = "update",
				description = "Ошибка обновления профиля отдела"
			)
		)
	}

}