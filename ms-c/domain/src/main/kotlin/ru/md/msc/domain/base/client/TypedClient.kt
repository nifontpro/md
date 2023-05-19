package ru.md.msc.domain.base.client

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.helper.otherError
import ru.md.msc.domain.base.biz.BaseClientContext

/**
 * Типизированная функция получения данных из другого микросервиса
 */
suspend inline fun <reified T : Any, reified R> BaseClientContext.getDataFromMs(
	uri: String,
	request: T
): R? {

	val result = microClient.getDataFromMs<R>(uri = uri, requestBody = request)
	if (!result.success) {
		errors.addAll(result.errors)
		return null
	} else {

		println(result.data)

		val data = result.data ?: run {
			fail(
				otherError(
					description = "Ошибка типа при получении данных из микросервиса",
					field = uri,
					level = ContextError.Levels.ERROR
				)
			)
			return null
		}
		return data
	}
}
