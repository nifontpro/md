package ru.md.shop.domain.pay.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.pay.biz.proc.PayContext

fun ICorChainDsl<PayContext>.validatePayDataId(title: String) = worker {
	this.title = title
	on { payDataId < 1 }
	handle {
		fail(
			errorValidation(
				field = "payDataId",
				violationCode = "not valid",
				description = "Неверный id платежной операции"
			)
		)
	}
}
