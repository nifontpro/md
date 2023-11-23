package ru.md.base_domain.errors

import ru.md.base_domain.biz.helper.*
import ru.md.base_domain.biz.proc.BaseMedalsContext

fun BaseMedalsContext.getAuthUserError() {
	fail(
		errorDb(
			repository = "user",
			violationCode = "get auth",
			description = "Ошибка получения авторизованного пользователя"
		)
	)
}

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

fun BaseMedalsContext.s3DeleteError() {
	fail(
		errorDb(
			repository = "s3",
			violationCode = "delete",
			description = "Ошибка удаления из хранилища объектов s3"
		)
	)
}

fun extMsGetDataError() = otherError(
	code = "ms-get error",
	description = "Ошибка получения данных из микросервиса",
	field = "ms",
	level = ContextError.Levels.ERROR
)

fun extMsGetATError() = otherError(
	code = "ms-get token error",
	description = "Ошибка получения токена для микросервиса",
	field = "ms",
	level = ContextError.Levels.ERROR
)

fun BaseMedalsContext.msGetDataError() {
	fail(
		extMsGetDataError()
	)
}


class ImageNotFoundException(message: String = "") : RuntimeException(message)
class S3DeleteException(message: String = "") : RuntimeException(message)