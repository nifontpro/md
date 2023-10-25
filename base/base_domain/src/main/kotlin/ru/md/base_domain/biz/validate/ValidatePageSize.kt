package ru.md.base_domain.biz.validate

import ru.md.base_domain.model.MAX_PAGE_SIZE
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.BaseMedalsContext

fun <T : BaseMedalsContext> ICorChainDsl<T>.validatePageSize(title: String) = worker {
	this.title = title
	on { baseQuery.pageSize < 1 || baseQuery.pageSize > MAX_PAGE_SIZE }
	handle {
		fail(
			errorValidation(
				field = "pageSize",
				violationCode = "not valid",
				description = "Размер страниц должен быть от 1 до $MAX_PAGE_SIZE"
			)
		)
	}
}
