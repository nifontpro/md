package ru.md.msgal.rest.base

import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.msgal.domain.base.biz.BaseGalleryContext
import kotlin.reflect.full.createInstance

/**
 * Функционал запрос-ответ сервера с применением бизнес-логики с авторизацией
 */
suspend inline fun <reified T, reified R, reified C : BaseGalleryContext> process(
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