package ru.md.msc.domain.dept.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.model.checkRepositoryData
import ru.md.msc.domain.dept.biz.proc.DeptContext

fun ICorChainDsl<DeptContext>.getSubtreeDepts(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		depts = checkRepositoryData {
			deptService.findSubTreeDepts(authUser.dept?.id ?: 0)
		} ?: return@handle
	}
}