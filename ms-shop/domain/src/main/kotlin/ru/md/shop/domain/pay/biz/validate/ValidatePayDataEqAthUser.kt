package ru.md.shop.domain.pay.biz.validate

import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.pay.biz.proc.PayContext

fun ICorChainDsl<PayContext>.validatePayDataEqAthUser(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING && payData.user.id != authUser.id }
	handle {
		fail(
			errorUnauthorized(
				role = "same user",
				message = "Нет доступа к выполнению операции"
			)
		)
	}
}
