package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.getUserError
import ru.md.msc.domain.user.biz.proc.userNotFound
import ru.md.msc.domain.user.model.RoleUser

fun ICorChainDsl<UserContext>.findUpdateUser(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		updateUser = try {
			userService.findById(userId = userId)
		} catch (e: Exception) {
			getUserError()
			return@handle
		} ?: run {
			userNotFound()
			return@handle
		}

		isUpdateUserHasAdminRole = authUser.roles.find { it == RoleUser.ADMIN } != null
		deptId = updateUser.dept?.id ?: 0 // Для авторизации по отделу
	}
}