package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.DeptContext

fun ICorChainDsl<DeptContext>.trimFieldDeptDetails(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		dept = dept.copy(
			name = dept.name.trim(),
			classname = dept.classname?.trim(),
		)

		deptDetails = deptDetails.copy(
			dept = dept,
			email = deptDetails.email?.trim(),
			phone = deptDetails.phone?.trim(),
			address = deptDetails.address?.trim(),
			description = deptDetails.description?.trim(),
		)
	}
}