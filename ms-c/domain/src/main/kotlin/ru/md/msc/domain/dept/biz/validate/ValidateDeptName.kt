package ru.md.msc.domain.dept.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.dept.biz.proc.DeptContext

fun ICorChainDsl<DeptContext>.validateDeptName(title: String) = worker {
	this.title = title
	on { dept.name.isBlank() }
	handle {
		fail(
			errorValidation(
				field = "name",
				violationCode = "blank",
				description = "Имя отдела не должно быть пустым"
			)
		)
	}
}
