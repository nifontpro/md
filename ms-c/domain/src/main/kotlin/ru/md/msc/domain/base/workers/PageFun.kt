package ru.md.msc.domain.base.workers

import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.model.PageResult

/**
 * Функция вызывает функцию репозитория с пагинацией, получает список данных
 * и сохраняет метаинформацию о пагинации в контексте
 */
fun <R> BaseContext.pageFun(
	func: () -> PageResult<R>
): List<R> {
	val res = func()
	pageInfo = res.pageInfo
	return res.data
}