package ru.md.msc.domain.user.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.RoleUser

fun ICorChainDsl<UserContext>.validateCreateUserRoles(title: String) = worker {
	this.title = title
	on {
		user.roles.forEach { role ->
			if (!(role == RoleUser.USER || role == RoleUser.ADMIN)) {
				notValidRole = role
				return@on true
			}
		}
		false
	}
	handle {
		fail(
			errorValidation(
				field = "role",
				violationCode = "not valid",
				description = "Недопустимая роль сотрудника $notValidRole"
			)
		)
	}
}
