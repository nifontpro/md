package ru.md.msc.domain.base.biz

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail


fun BaseClientContext.notValidAuthIdError() {
	fail(
		errorUnauthorized(
			message = "Неверный authId",
		)
	)
}

fun BaseClientContext.imageNotFoundError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "image not found",
			description = "Изображение не найдено",
			level = ContextError.Levels.INFO,
		)
	)
}

fun BaseClientContext.addImageError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "image add",
			description = "Ошибка добавления изображения"
		)
	)
}

fun BaseClientContext.deleteImageError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "delete",
			description = "Ошибка удаления изображения"
		)
	)
}

fun BaseClientContext.updateMainImageError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "update main image",
			description = "Ошибка обновления основного изображения"
		)
	)
}

fun BaseClientContext.s3Error() {
	fail(
		errorDb(
			repository = "s3",
			violationCode = "i/o",
			description = "Ошибка хранилища объектов"
		)
	)
}

class ImageNotFoundException(message: String = "") : RuntimeException(message)