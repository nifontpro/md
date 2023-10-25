package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.base_domain.user.model.RoleUser

fun ICorChainDsl<UserContext>.createOwner(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		val userRoles = setOf(RoleUser.OWNER, RoleUser.ADMIN)
		userDetails = userDetails.copy(user = user.copy(roles = userRoles))
		val res = userService.createOwner(userDetails)
		userDetails = res.userDetails
		deptId = res.deptId
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "user",
				violationCode = "owner create",
				description = "Ошибка создания профиля владельца"
			)
		)
	}

}