package ru.md.shop.domain.product.biz.proc

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail

fun ProductContext.productNotFoundError() {
	fail(
		errorDb(
			repository = "product",
			violationCode = "not found",
			description = "Приз не найден",
			level = ContextError.Levels.INFO
		)
	)
}

fun ProductContext.getProductError() {
	fail(
		errorDb(
			repository = "product",
			violationCode = "get error",
			description = "Ошибка получения приза"
		)
	)
}

//fun AwardContext.getAwardCountError() {
//	fail(
//		errorDb(
//			repository = "award",
//			violationCode = "count error",
//			description = "Ошибка получения количества наград"
//		)
//	)
//}
//

class ProductNotFoundException(message: String = "") : RuntimeException(message)
//class AlreadyActionException(message: String = "") : RuntimeException(message)
