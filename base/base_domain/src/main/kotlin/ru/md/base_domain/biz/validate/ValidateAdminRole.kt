package ru.md.base_domain.biz.validate

import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

fun <T : BaseMedalsContext> ICorChainDsl<T>.validateAdminRole(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING && !isAuthUserHasAdminRole }
	handle {
		fail(
			errorUnauthorized(
				role = "ADMIN",
				message = "Для выполнения операции нужны права Администратора",
			)
		)
	}
}
