package ru.md.shop.domain.product.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext

fun ICorChainDsl<ProductContext>.validateProductId(title: String) = worker {
	this.title = title
	on { productId < 1 }
	handle {
		fail(
			errorValidation(
				field = "productId",
				violationCode = "not valid",
				description = "Неверный id приза"
			)
		)
	}
}
