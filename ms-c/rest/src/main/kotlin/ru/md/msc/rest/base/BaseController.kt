package ru.md.msc.rest.base

import ru.md.base.dom.biz.BaseContext
import ru.md.base.dom.biz.IBaseProcessor
import kotlin.reflect.full.createInstance

/**
 * Функционал запрос-ответ сервера с применением бизнес-логики с авторизацией
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