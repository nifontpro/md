package ru.md.msc.rest.base

import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.IBaseProcessor
import ru.md.msc.domain.base.helper.ContextError
import kotlin.reflect.full.createInstance

/**
 * Функционал запрос-ответ сервера с применением бизнес-логики с авторизацией
 */
suspend inline fun <reified T, reified R, reified C : BaseContext> process(
	processor: IBaseProcessor<C>,
	baseRequest: BaseRequest<T>,
	fromTransport: C.(T) -> Unit,
	toTransport: C.() -> R
): R {
	val context = C::class.createInstance()
	println("emailVerified: ${baseRequest.emailVerified}")
	println("email: ${baseRequest.authEmail}")

	if (!baseRequest.emailVerified || baseRequest.authEmail.isNullOrBlank()) {
		context.state = ContextState.FAILING
		context.errors.add(
			ContextError(
				code = "email not verified",
				group = "auth",
				field = "email",
				level = ContextError.Levels.UNAUTHORIZED,
				message = "Непроверенная электронная почта"
			)
		)
		return context.toTransport()
	}
	context.authEmail = baseRequest.authEmail
	context.fromTransport(baseRequest.data)
	processor.exec(context)
	return context.toTransport()
}