package ru.md.shop.domain.pay.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.pay.biz.proc.*
import ru.md.shop.domain.product.biz.proc.ProductNotFoundException
import ru.md.shop.domain.product.biz.proc.productNotFoundError

fun ICorChainDsl<PayContext>.addOperation(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		payData = payService.addOperation(payData = payData, payCode = payCode)
	}

	except {
		log.error(it.message)
		when (it) {
			is ProductNotFoundException -> productNotFoundError()
			is UserPayNotFoundException -> userPayNotFoundError()
			is InsufficientUserBalanceException -> insufficientUserBalanceError()
			else -> payProductError()
		}
	}
}