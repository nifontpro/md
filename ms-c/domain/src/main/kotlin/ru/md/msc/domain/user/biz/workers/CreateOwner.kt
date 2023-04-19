package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.RoleUser

fun ICorChainDsl<UserContext>.createOwner(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		val userRoles = setOf(RoleUser.OWNER, RoleUser.ADMIN)
		userDetails = userDetails.copy(user = user.copy(roles = userRoles))

		userDetails = try {
			userService.createOwner(userDetails)
		} catch (e: Exception) {
			fail(
				errorDb(
					repository = "user",
					violationCode = "owner create",
					description = "Ошибка создания профиля владельца"
				)
			)
			return@handle
		}
	}
}