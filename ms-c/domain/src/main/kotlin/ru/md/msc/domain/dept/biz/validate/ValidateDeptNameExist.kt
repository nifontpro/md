package ru.md.msc.domain.dept.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.biz.proc.getDeptError

fun ICorChainDsl<DeptContext>.validateDeptNameExist(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {
		if (deptService.checkDeptExist(parentId = dept.parentId, name = dept.name)) {
			fail(
				errorValidation(
					field = "dept name",
					violationCode = "exist",
					description = "Отдел с таким названием уже существует на этом уровне"
				)
			)
		}
	}

	except {
		log.error(it.message)
		getDeptError()
	}
}
