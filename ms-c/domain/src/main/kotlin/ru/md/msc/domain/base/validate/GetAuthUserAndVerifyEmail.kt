package ru.md.msc.domain.base.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorUnauthorized
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.base.model.checkRepositoryData

fun <T : BaseContext> ICorChainDsl<T>.getAuthUserAndVerifyEmail(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		authUser = checkRepositoryData {
			userService.getById(authId)
		} ?: run {
			fail(
				errorUnauthorized(
					message = "Авторизованный пользователь не найден",
				)
			)
			return@handle
		}

		println("AUTH USER: $authUser")
	}
}
