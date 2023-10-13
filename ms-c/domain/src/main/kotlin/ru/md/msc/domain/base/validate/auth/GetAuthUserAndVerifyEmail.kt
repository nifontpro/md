package ru.md.msc.domain.base.validate.auth

import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.base.biz.notValidAuthIdError
import ru.md.msc.domain.user.biz.proc.getUserError

fun <T : BaseClientContext> ICorChainDsl<T>.getAuthUserAndVerifyEmail(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		if (authId < 1) {
			notValidAuthIdError()
			return@handle
		}

		authUser = userService.findById(authId) ?: run {
			notValidAuthIdError()
			return@handle
		}

		log.info("authUser.authEmail ${authUser.authEmail}")
		log.info("authEmail $authEmail")


		if (authUser.authEmail != authEmail) {
			fail(
				errorUnauthorized(
					role = "access authId",
					message = "Доступ по authId запрещен",
				)
			)
			return@handle
		}
	}

	except {
		getUserError()
	}
}
