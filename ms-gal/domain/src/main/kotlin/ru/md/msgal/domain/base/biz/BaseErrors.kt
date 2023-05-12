package ru.md.msgal.domain.base.biz

import ru.md.msgal.domain.base.helper.ContextError
import ru.md.msgal.domain.base.helper.errorDb
import ru.md.msgal.domain.base.helper.errorUnauthorized
import ru.md.msgal.domain.base.helper.fail


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

fun BaseContext.addImageError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "image add",
			description = "Ошибка добавления изображения"
		)
	)
}

fun BaseContext.deleteImageError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "delete",
			description = "Ошибка удаления изображения"
		)
	)
}

fun BaseContext.s3Error() {
	fail(
		errorDb(
			repository = "s3",
			violationCode = "i/o",
			description = "Ошибка хранилища объектов"
		)
	)
}

fun BaseContext.mustPageableError() {
	fail(
		errorDb(
			repository = "base",
			violationCode = "pageable",
			description = "Для этого метода обязательны параметры пагинации"
		)
	)
}

class ImageNotFoundException(message: String = "") : RuntimeException(message)
class MustPageableException(message: String = "") : RuntimeException(message)