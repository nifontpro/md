package ru.md.msgal.rest.base

import ru.md.msgal.domain.base.biz.BaseContext
import ru.md.msgal.domain.base.biz.ContextState
import ru.md.msgal.domain.base.helper.ContextError

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