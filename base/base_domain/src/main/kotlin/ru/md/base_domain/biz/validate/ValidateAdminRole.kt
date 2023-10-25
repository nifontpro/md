package ru.md.base_domain.biz.validate

import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.user.model.RoleUser

fun <T : BaseMedalsContext> ICorChainDsl<T>.validateAdminRole(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		isAuthUserHasAdminRole = authUser.roles.find { it == RoleUser.ADMIN } != null

		if (!isAuthUserHasAdminRole) {
			fail(
				errorUnauthorized(
					role = "ADMIN",
					message = "Для выполнения операции нужны права Администратора",
				)
			)
		}

	}
}
