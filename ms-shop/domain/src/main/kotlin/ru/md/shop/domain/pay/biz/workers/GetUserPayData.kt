package ru.md.shop.domain.pay.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.pay.model.UserPay
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.pay.biz.proc.PayContext
import ru.md.shop.domain.pay.biz.proc.UserPayNotFoundException
import ru.md.shop.domain.pay.biz.proc.getUserPayError

fun ICorChainDsl<PayContext>.getUserPayData(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userPay = payService.getUserPayData(userId)
	}

	except {
		log.error(it.message)
		when (it) {
			is UserPayNotFoundException -> userPay = UserPay(userId = userId)
			else -> getUserPayError()
		}
	}
}