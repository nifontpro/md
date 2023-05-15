package ru.md.msc.domain.base.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail

fun <T : BaseClientContext> ICorChainDsl<T>.validateDeptId(title: String) = worker {
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
