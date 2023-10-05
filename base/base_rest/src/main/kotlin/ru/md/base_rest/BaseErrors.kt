package ru.md.base_rest

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.proc.BaseContext
import ru.md.base_domain.biz.proc.ContextState

fun <C : BaseContext> C.emailNotVerified() {
	state = ContextState.FAILING
	errors.add(
		ContextError(
			code = "email not verified",
			group = "auth",
			field = "email",
			level = ContextError.Levels.UNAUTHORIZED,
			message = "Непроверенная электронная почта"
		)
	)
}

private const val IMG_ERR = "Ошибка записи в файловую систему сервера"
fun <C : BaseContext> C.imageSaveError(message: String? = IMG_ERR) {
	state = ContextState.FAILING
	errors.add(
		ContextError(
			code = "image save error",
			group = "internal",
			field = "file",
			level = ContextError.Levels.ERROR,
			message = message ?: IMG_ERR
		)
	)
}

fun <C : BaseContext> C.fileContentTypeError(type: String) {
	state = ContextState.FAILING
	errors.add(
		ContextError(
			code = "file content type error",
			group = "type",
			field = type,
			level = ContextError.Levels.ERROR,
			message = "Загружаемый файл должен быть изображением"
		)
	)
}

fun String.toLongOr0(): Long = this.toLongOrNull() ?: 0