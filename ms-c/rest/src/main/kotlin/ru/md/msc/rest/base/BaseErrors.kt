package ru.md.msc.rest.base

import ru.md.base_domain.biz.proc.ContextState
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.biz.helper.ContextError

fun <C : BaseClientContext> C.emailNotVerified() {
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

fun <C : BaseClientContext> C.fileSaveError() {
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

fun String.toLongOr0(): Long = this.toLongOrNull() ?: 0