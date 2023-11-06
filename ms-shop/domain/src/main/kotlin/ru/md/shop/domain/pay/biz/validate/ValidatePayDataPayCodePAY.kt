package ru.md.shop.domain.pay.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.pay.biz.proc.PayContext
import ru.md.shop.domain.pay.model.PayCode

fun ICorChainDsl<PayContext>.validatePayDataPayCodePAY(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING && payData.payCode != PayCode.PAY && !payData.isActive }
	handle {
		fail(
			errorValidation(
				field = "payCode",
				violationCode = "not PAY",
				description = "Выбранная операция должна быть в состоянии 'Приз куплен' (PAY) и активна"
			)
		)
	}
}
