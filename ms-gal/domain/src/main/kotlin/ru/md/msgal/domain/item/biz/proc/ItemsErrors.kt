package ru.md.msgal.domain.item.biz.proc

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail

fun ItemContext.itemNotFoundError() {
	fail(
		errorDb(
			repository = "item",
			violationCode = "not found",
			description = "Объект не найден"
		)
	)
}

fun ItemContext.getItemsError() {
	fail(
		errorDb(
			repository = "item",
			violationCode = "get error",
			description = "Ошибка чтения объектов"
		)
	)
}

class ItemNotFoundException(message: String = "Объект не найден") : RuntimeException(message)
