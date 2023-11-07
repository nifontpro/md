package ru.md.shop.domain.pay.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.pay.biz.proc.PayContext

fun ICorChainDsl<PayContext>.setGetPaysDataSortedFields(title: String) = worker {
	this.title = title
	handle {
		orderFields = listOf(
			"id",
			"dateOp",
			"payCode",
			"price",
			"isActive",
			"userEntity.firstname",
			"userEntity.lastname",
			"userEntity.dept.name",
			"productEntity.name",
			"productEntity.price",
			"productEntity.count",
		)
	}
}
