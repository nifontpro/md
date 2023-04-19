package ru.md.msc.domain.base.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail

fun <T : BaseContext> ICorChainDsl<T>.validateUserId(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		if (userId < 1) {
			fail(
				errorValidation(
					field = "userId",
					violationCode = "not valid",
					description = "Неверный userId"
				)
			)
			return@handle
		}

	}
}
