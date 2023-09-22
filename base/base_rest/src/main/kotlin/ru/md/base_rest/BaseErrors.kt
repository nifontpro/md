package ru.md.base_rest

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.proc.BaseContext

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

fun <C : BaseContext> C.fileSaveError() {
	state = ContextState.FAILING
	errors.add(
		ContextError(
			code = "file save error",
			group = "internal",
			field = "file",
			level = ContextError.Levels.ERROR,
			message = "Ошибка записи в файловую систему сервера"
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