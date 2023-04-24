package ru.md.msc.domain.base.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorUnauthorized
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.user.model.RoleUser

fun <T : BaseContext> ICorChainDsl<T>.validateAdminRole(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		isAuthUserHasAdminRole = authUser.roles.find { it == RoleUser.ADMIN } != null

		if (!isAuthUserHasAdminRole) {
			fail(
				errorUnauthorized(
					role = "ADMIN",
					message = "Для обновления профиля другого сотрудника нужны права Администратора",
				)
			)
		}

	}
}
