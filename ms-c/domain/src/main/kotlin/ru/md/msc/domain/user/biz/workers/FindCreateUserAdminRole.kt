package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.RoleUser

fun ICorChainDsl<UserContext>.findCreateUserAdminRole(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {
		isModifyUserHasAdminRole = user.roles.find { it == RoleUser.ADMIN } != null
	}
}