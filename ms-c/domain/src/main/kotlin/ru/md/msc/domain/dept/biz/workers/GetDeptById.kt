package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.biz.proc.deptNotFound
import ru.md.msc.domain.dept.biz.proc.getDeptError

fun ICorChainDsl<DeptContext>.getDeptById(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		dept = deptService.findById(deptId = deptId) ?: run {
			deptNotFound()
			return@handle
		}
	}

	except {
		log.error(it.message)
		getDeptError()
	}

}