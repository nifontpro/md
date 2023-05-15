package ru.md.msgal.domain.base.biz

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.errorUnauthorized
import ru.md.base_domain.biz.helper.fail


fun BaseGalleryContext.notValidAuthIdError() {
	fail(
		errorUnauthorized(
			message = "Неверный authId",
		)
	)
}

fun BaseGalleryContext.imageNotFoundError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "image not found",
			description = "Изображение не найдено",
			level = ContextError.Levels.INFO,
		)
	)
}

fun BaseGalleryContext.addImageError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "image add",
			description = "Ошибка добавления изображения"
		)
	)
}

fun BaseGalleryContext.deleteImageError() {
	fail(
		errorDb(
			repository = "image",
			violationCode = "delete",
			description = "Ошибка удаления изображения"
		)
	)
}

fun BaseGalleryContext.s3Error() {
	fail(
		errorDb(
			repository = "s3",
			violationCode = "i/o",
			description = "Ошибка хранилища объектов"
		)
	)
}

fun BaseGalleryContext.mustPageableError() {
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