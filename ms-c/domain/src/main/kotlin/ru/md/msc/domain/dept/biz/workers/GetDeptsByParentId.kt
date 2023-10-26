package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.base_domain.dept.biz.errors.getDeptError

fun ICorChainDsl<DeptContext>.getDeptsByParentId(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		depts = deptService.getDeptsByParentId(parentId = deptId, orders = baseQuery.orders)
	}

	except {
		log.error(it.message)
		getDeptError()
	}
}