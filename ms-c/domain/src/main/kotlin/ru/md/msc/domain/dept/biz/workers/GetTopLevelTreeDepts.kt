package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.base_domain.dept.biz.errors.TopLevelDeptNotFoundException
import ru.md.base_domain.dept.biz.errors.getDeptError
import ru.md.base_domain.dept.biz.errors.topLevelDeptNotFound

fun ICorChainDsl<DeptContext>.getTopLevelTreeDepts(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		depts = deptService.getTopLevelTreeDepts(deptId = authUser.dept?.id ?: 0, orders = baseQuery.orders)
	}

	except {
		log.error(it.message)
		when (it) {
			is TopLevelDeptNotFoundException -> topLevelDeptNotFound()
			else -> getDeptError()
		}
	}
}