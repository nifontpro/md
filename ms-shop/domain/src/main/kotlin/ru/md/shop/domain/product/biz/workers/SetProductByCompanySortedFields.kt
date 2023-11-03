package ru.md.shop.domain.product.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext

fun ICorChainDsl<ProductContext>.setProductByCompanySortedFields(title: String) = worker {
	this.title = title
	handle {
		orderFields = listOf(
			"name",
			"price",
			"count",
			"description",
		)
	}
}
