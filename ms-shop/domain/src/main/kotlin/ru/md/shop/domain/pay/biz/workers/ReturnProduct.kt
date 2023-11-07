package ru.md.shop.domain.pay.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.pay.biz.proc.PayContext
import ru.md.shop.domain.pay.biz.proc.addPayDataError

fun ICorChainDsl<PayContext>.returnProduct(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		payData = payService.returnProduct(payData)
	}

	except {
		log.error(it.message)
		addPayDataError()
	}
}