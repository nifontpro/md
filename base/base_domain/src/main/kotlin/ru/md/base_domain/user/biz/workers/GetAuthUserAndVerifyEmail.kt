package ru.md.base_domain.user.biz.workers

import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.errors.getAuthUserError
import ru.md.base_domain.errors.notValidAuthIdError
import ru.md.base_domain.user.model.RoleUser
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

fun <T : BaseMedalsContext> ICorChainDsl<T>.getAuthUserAndVerifyEmail(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		if (authId < 1) {
			notValidAuthIdError()
			return@handle
		}

		authUser = baseUserService.findById(authId) ?: run {
			notValidAuthIdError()
			return@handle
		}

		log.info("authUser: $authUser")

		if (authUser.authEmail != authEmail) {
			fail(
				errorUnauthorized(
					role = "access authId",
					message = "Доступ по authId запрещен",
				)
			)
			return@handle
		}

		isAuthUserHasAdminRole = authUser.roles.find { it == RoleUser.ADMIN } != null
	}

	except {
		log.error(it.message)
		getAuthUserError()
	}
}
