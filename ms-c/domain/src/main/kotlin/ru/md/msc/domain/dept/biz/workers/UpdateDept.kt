package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
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