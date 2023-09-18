package ru.md.base_rest

import ru.md.base_domain.biz.proc.BaseContext
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_rest.model.request.AuthRequest
import kotlin.reflect.full.createInstance

/**
 * Функционал запрос-ответ сервера с применением бизнес-логики с авторизацией
 */
suspend inline fun <reified T, reified R, reified C : BaseContext> authProcess(
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

/**
 * Функционал запрос-ответ сервера с применением бизнес-логики без авторизации
 * Для обмена между микросервисами
 */
suspend inline fun <reified T, reified R, reified C : BaseContext> process(
	processor: IBaseProcessor<C>,
	request: T,
	fromTransport: C.(T) -> Unit,
	toTransport: C.() -> R
): R {
	val context = C::class.createInstance()
	context.fromTransport(request)
	processor.exec(context)
	return context.toTransport()
}