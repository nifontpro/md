package ru.md.base_domain.errors

import ru.md.base_domain.biz.helper.*
import ru.md.base_domain.biz.proc.BaseMedalsContext


fun BaseMedalsContext.notValidAuthIdError() {
	fail(
		errorUnauthorized(
			role = "authId not valid",
			message = "Неверный authId",
		)
	)
}

fun BaseMedalsContext.imageNotFoundError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "image not found",
			description = "Изображение не найдено",
			level = ContextError.Levels.INFO,
		)
	)
}

fun BaseMedalsContext.addImageError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "image add",
			description = "Ошибка добавления изображения"
		)
	)
}

fun BaseMedalsContext.deleteImageError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "delete",
			description = "Ошибка удаления изображения"
		)
	)
}

fun BaseMedalsContext.updateMainImageError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "update main image",
			description = "Ошибка обновления основного изображения"
		)
	)
}

fun BaseMedalsContext.s3Error() {
	fail(
		errorDb(
			repository = "s3",
			violationCode = "i/o",
			description = "Ошибка хранилища объектов"
		)
	)
}

fun BaseMedalsContext.getGalleryItemMsError() {
	fail(
		otherError(
			code = "ms-get gallery item",
			description = "Ошибка получения объекта из микросервиса галереи",
			field = "gallery",
			level = ContextError.Levels.ERROR
		)
	)
}

class ImageNotFoundException(message: String = "") : RuntimeException(message)