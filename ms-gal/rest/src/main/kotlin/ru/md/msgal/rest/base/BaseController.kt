package ru.md.msgal.rest.base

import ru.md.msgal.domain.base.biz.BaseContext
import ru.md.msgal.domain.base.biz.IBaseProcessor
import kotlin.reflect.full.createInstance

/**
 * Функционал запрос-ответ сервера с применением бизнес-логики с авторизацией
 */
suspend inline fun <reified T, reified R, reified C : BaseContext> process(
	processor: IBaseProcessor<C>,
	authRequest: AuthRequest<T>,
	fromTransport: C.(T) -> Unit,
	toTransport: C.() -> R
): R {
	val context = C::class.createInstance()

	if (!authRequest.emailVerified || authRequest.authEmail.isNullOrBlank()) {
		context.emailNotVerified()
		return context.toTransport()
	}
	context.authEmail = authRequest.authEmail
	context.fromTransport(authRequest.data)
	processor.exec(context)
	return context.toTransport()
}