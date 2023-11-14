package ru.md.shop.domain.pay.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.pay.biz.proc.PayContext

fun ICorChainDsl<PayContext>.validateActivePayData(title: String) = worker {
	this.title = title
	on { !payData.isActive }
	handle {
		fail(
			errorValidation(
				field = "isActive",
				violationCode = "false",
				description = "Выбор недоступной платежной операции"
			)
		)
	}
}
