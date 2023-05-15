package ru.md.msgal.rest.base

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.proc.ContextState
import ru.md.msgal.domain.base.biz.BaseGalleryContext

fun <C : BaseGalleryContext> C.emailNotVerified() {
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

fun <C : BaseGalleryContext> C.fileSaveError() {
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