package ru.md.msc.domain.base.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail

fun <T : BaseContext> ICorChainDsl<T>.validatePageSize(title: String) = worker {
	this.title = title
	on { baseQuery.pageSize?.let { it < 1 } ?: false }
	handle {
		fail(
			errorValidation(
				field = "pageSize",
				violationCode = "not valid",
				description = "Неверный размер страниц"
			)
		)
	}
}
