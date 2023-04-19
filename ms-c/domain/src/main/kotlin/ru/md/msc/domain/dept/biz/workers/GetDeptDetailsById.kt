package ru.md.msc.domain.dept.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.biz.proc.deptNotFound
import ru.md.msc.domain.dept.biz.proc.getDeptError

fun ICorChainDsl<DeptContext>.getDeptDetailsById(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		val deptDetailsNull = try {
			deptService.findByIdDetails(deptId = deptId)
		} catch (e: Exception) {
			getDeptError()
			return@handle
		}

		if (deptDetailsNull == null) {
			deptNotFound()
		} else {
			deptDetails = deptDetailsNull
		}

	}
}