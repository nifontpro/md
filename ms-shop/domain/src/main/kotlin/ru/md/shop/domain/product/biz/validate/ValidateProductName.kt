package ru.md.shop.domain.product.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext

fun ICorChainDsl<ProductContext>.validateProductName(title: String) = worker {
	this.title = title
	on { product.name.isBlank() }
	handle {
		fail(
			errorValidation(
				field = "name",
				violationCode = "blank",
				description = "Название приза не должно быть пустым"
			)
		)
	}
}
