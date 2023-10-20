package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.userUpdateError

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
		userUpdateError()
	}

}