package ru.md.msc.domain.base.biz

import ru.md.msc.domain.base.helper.ContextError
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.errorUnauthorized
import ru.md.msc.domain.base.helper.fail


fun BaseContext.notValidAuthIdError() {
	fail(
		errorUnauthorized(
			message = "Неверный authId",
		)
	)
}

fun BaseContext.imageNotFoundError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "image not found",
			description = "Изображение не найдено",
			level = ContextError.Levels.INFO,
		)
	)
}

fun BaseContext.updateImageError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "image update",
			description = "Ошибка обновления изображения"
		)
	)
}