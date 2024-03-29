package ru.md.msc.domain.base.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.user.biz.errors.getUserError
import ru.md.base_domain.user.biz.errors.userNotFoundError
import ru.md.base_domain.user.model.RoleUser

fun <T : BaseClientContext> ICorChainDsl<T>.findModifyUserAndGetRolesAndDeptId(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		modifyUser = baseUserService.findById(userId = userId) ?: run {
			userNotFoundError()
			return@handle
		}

		isModifyUserHasAdminRole = modifyUser.roles.find { it == RoleUser.ADMIN } != null
		deptId = modifyUser.dept?.id ?: 0 // Для авторизации по отделу
	}

	except {
		log.error(it.message)
		getUserError()
	}

}