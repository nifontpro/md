package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.updateUser(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
//		if (user.roles.isEmpty()) { // при пустом значении роли не меняются
//			userDetails = userDetails.copy(
//				user = user.copy(roles = modifyUser.roles)
//			)
//		}
		userDetails = userService.update(userDetails = userDetails, isAuthUserHasAdminRole = isAuthUserHasAdminRole)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "user",
				violationCode = "update",
				description = "Ошибка обновления профиля сотрудника"
			)
		)
	}

}