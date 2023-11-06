package ru.md.shop.domain.pay.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.pay.biz.proc.*
import ru.md.shop.domain.product.biz.proc.ProductNotFoundException
import ru.md.shop.domain.product.biz.proc.productNotFoundError

fun ICorChainDsl<PayContext>.payProduct(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		payData = payService.payProduct(productId = productId, userId = authUser.id).copy(
			user = authUser
		)
	}

	except {
		log.error(it.message)
		when (it) {
			is ProductNotFoundException -> productNotFoundError()
			is UserPayNotFoundException -> userPayNotFoundError()
			is InsufficientUserBalanceException -> insufficientUserBalanceError()
			else -> addPayDataError()
		}
	}
}