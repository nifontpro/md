package ru.md.shop.domain.product.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext

private const val MIN_PRICE = 1
private const val MAX_PRICE = 10000

fun ICorChainDsl<ProductContext>.validateProductPrice(title: String) = worker {
	this.title = title
	on { product.price < MIN_PRICE || product.price > MAX_PRICE }
	handle {
		fail(
			errorValidation(
				field = "price",
				violationCode = "out of range",
				description = "Цена приза должна быть от $MIN_PRICE до $MAX_PRICE"
			)
		)
	}
}
