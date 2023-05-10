package ru.md.msc.domain.base.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail

fun <T : BaseContext> ICorChainDsl<T>.validatePageNumber(title: String) = worker {
	this.title = title
	on { baseQuery.page < 0 }
	handle {
		fail(
			errorValidation(
				field = "page",
				violationCode = "not valid",
				description = "Неверный номер страницы"
			)
		)
	}
}
