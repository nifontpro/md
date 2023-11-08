package ru.md.shop.domain.product.biz.proc

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.shop.domain.base.biz.proc.ShopContext

fun ShopContext.productNotFoundError() {
	fail(
		errorDb(
			repository = "product",
			violationCode = "not found",
			description = "Приз не найден",
			level = ContextError.Levels.INFO
		)
	)
}

fun ShopContext.getProductError() {
	fail(
		errorDb(
			repository = "product",
			violationCode = "get error",
			description = "Ошибка получения приза"
		)
	)
}

fun ShopContext.insufficientProductError() {
	fail(
		errorDb(
			repository = "product",
			violationCode = "insufficient",
			description = "Приза нет в наличии"
		)
	)
}

class ProductNotFoundException(message: String = "") : RuntimeException(message)
class InsufficientProductQuantityException(message: String = "") : RuntimeException(message)
