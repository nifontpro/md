package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.biz.proc.getDeptError

fun ICorChainDsl<DeptContext>.getSubtreeDepts(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		depts = deptService.findSubTreeDepts(deptId = authUser.dept?.id ?: 0, orders = baseQuery.orders)
	}

	except {
		log.error(it.message)
		getDeptError()
	}
}