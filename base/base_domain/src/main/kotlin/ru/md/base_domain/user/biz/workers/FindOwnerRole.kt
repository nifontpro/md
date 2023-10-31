package ru.md.base_domain.user.biz.workers

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.user.model.RoleUser

fun <T : BaseMedalsContext> ICorChainDsl<T>.findOwnerRole(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {
		isAuthUserHasOwnerRole = authUser.roles.find { it == RoleUser.OWNER } != null
	}
}
