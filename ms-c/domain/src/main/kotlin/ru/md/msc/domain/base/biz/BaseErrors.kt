package ru.md.msc.domain.base.biz

import ru.md.base_domain.biz.helper.*


fun BaseClientContext.notValidAuthIdError() {
	log.error("notValidAuthIdError: authId=$authId")
	fail(
		errorUnauthorized(
			role = "authId not valid",
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

fun BaseClientContext.getGalleryItemMsError() {
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