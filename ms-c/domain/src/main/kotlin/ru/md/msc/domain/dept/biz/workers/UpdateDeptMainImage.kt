package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.errors.updateMainImageError
import ru.md.msc.domain.dept.biz.proc.DeptContext

fun ICorChainDsl<DeptContext>.updateDeptMainImage(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		deptService.setMainImage(deptId = deptId)
	}

	except {
		log.error(it.message)
		updateMainImageError()
	}

}