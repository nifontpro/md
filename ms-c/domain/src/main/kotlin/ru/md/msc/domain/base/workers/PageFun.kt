package ru.md.msc.domain.base.workers

import ru.md.base_domain.model.PageResult
import ru.md.msc.domain.base.biz.BaseClientContext

/**
 * Функция вызывает функцию репозитория с пагинацией, получает список данных
 * и сохраняет метаинформацию о пагинации в контексте
 */
fun <R> BaseClientContext.pageFun(
	func: () -> PageResult<R>
): List<R> {
	val res = func()
	pageInfo = res.pageInfo
	return res.data
}