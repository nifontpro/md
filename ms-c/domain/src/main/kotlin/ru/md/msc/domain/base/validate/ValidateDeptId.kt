package ru.md.msc.domain.base.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail

fun <T : BaseContext> ICorChainDsl<T>.validateDeptId(title: String) = worker {
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
