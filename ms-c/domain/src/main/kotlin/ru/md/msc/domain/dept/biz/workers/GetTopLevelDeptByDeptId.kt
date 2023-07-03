package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.*

fun ICorChainDsl<DeptContext>.getTopLevelDeptByDeptId(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		dept = deptService.findTopLevelDept(deptId = authUser.dept?.id ?: 0)
		log.error(dept.toString())
	}

	except {
		log.error(it.message)
		when (it) {
			is TopLevelDeptNotFoundException -> topLevelDeptNotFound()
			else -> getDeptError()
		}
	}

}