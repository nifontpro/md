package ru.md.base_domain.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.BaseMedalsContext

fun <T : BaseMedalsContext> ICorChainDsl<T>.validateDeptId(title: String) = worker {
	this.title = title
	on { deptId < 1 }
	handle {
		fail(
			errorValidation(
				field = "deptId",
				violationCode = "not valid",
				description = "Неверный deptId"
			)
		)
	}
}
