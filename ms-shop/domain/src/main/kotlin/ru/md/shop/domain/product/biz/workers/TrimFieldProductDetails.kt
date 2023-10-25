package ru.md.shop.domain.product.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext
import java.time.LocalDateTime

fun ICorChainDsl<ProductContext>.trimFieldProductDetails(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		product = product.copy(
			name = product.name.trim(),
		)

		productDetails = productDetails.copy(
			product = product,
			description = productDetails.description?.trim(),
			siteUrl = productDetails.siteUrl?.trim(),
			createdAt = LocalDateTime.now()
		)
	}
}