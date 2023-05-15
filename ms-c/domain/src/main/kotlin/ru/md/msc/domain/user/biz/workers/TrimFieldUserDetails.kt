package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.trimFieldUserDetails(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		user = user.copy(
			firstname = user.firstname.trim(),
			lastname = user.lastname?.trim(),
			patronymic = user.patronymic?.trim(),
			authEmail = user.authEmail?.trim(),
			post = user.post?.trim(),
		)

		userDetails = userDetails.copy(
			user = user,
			phone = userDetails.phone?.trim(),
			address = userDetails.address?.trim(),
			description = userDetails.description?.trim(),
		)
	}
}