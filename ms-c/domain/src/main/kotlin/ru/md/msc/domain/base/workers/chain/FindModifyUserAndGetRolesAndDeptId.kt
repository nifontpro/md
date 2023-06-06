package ru.md.msc.domain.base.workers.chain

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.user.biz.proc.getUserError
import ru.md.msc.domain.user.biz.proc.userNotFoundError
import ru.md.msc.domain.user.model.RoleUser

fun <T : BaseClientContext> ICorChainDsl<T>.findModifyUserAndGetRolesAndDeptId(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		modifyUser = userService.findById(userId = userId) ?: run {
			userNotFoundError()
			return@handle
		}

		isModifyUserHasAdminRole = authUser.roles.find { it == RoleUser.ADMIN } != null
		deptId = modifyUser.dept?.id ?: 0 // Для авторизации по отделу
	}

	except {
		log.error(it.message)
		getUserError()
	}

}