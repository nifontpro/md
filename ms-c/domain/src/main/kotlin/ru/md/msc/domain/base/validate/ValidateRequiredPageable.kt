package ru.md.msc.domain.base.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail

fun <T : BaseContext> ICorChainDsl<T>.validateRequiredPageable(title: String) = worker {
	this.title = title
	on { baseQuery.page == null || baseQuery.pageSize == null }
	handle {
		fail(
			errorValidation(
				field = "pageable",
				violationCode = "required",
				description = "Для данного метода обязательна пагинация"
			)
		)
	}
}
