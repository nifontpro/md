package ru.md.shop.domain.product.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext

fun ICorChainDsl<ProductContext>.validateProductCount(title: String) = worker {
	this.title = title
	on { product.count < 0 }
	handle {
		fail(
			errorValidation(
				field = "count",
				violationCode = "out of range",
				description = "Количество должно быть положительным"
			)
		)
	}
}
