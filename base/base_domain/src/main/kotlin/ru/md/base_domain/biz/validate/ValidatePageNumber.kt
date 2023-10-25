package ru.md.base_domain.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.BaseMedalsContext

fun <T : BaseMedalsContext> ICorChainDsl<T>.validatePageNumber(title: String) = worker {
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
