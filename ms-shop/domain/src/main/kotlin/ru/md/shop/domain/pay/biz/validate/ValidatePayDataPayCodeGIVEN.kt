package ru.md.shop.domain.pay.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.pay.biz.proc.PayContext
import ru.md.shop.domain.pay.model.PayCode

fun ICorChainDsl<PayContext>.validatePayDataPayCodeGIVENorPAY(title: String) = worker {
	this.title = title
	on {
		!(payData.payCode == PayCode.GIVEN || payData.payCode == PayCode.PAY)
	}
	handle {
		fail(
			errorValidation(
				field = "payCode",
				violationCode = "must GIVEN or PAY",
				description = "Приз должен быть выдан или куплен"
			)
		)
	}
}
