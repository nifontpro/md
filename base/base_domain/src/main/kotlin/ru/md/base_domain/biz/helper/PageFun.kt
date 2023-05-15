package ru.md.base_domain.biz.helper

import ru.md.base_domain.biz.proc.BaseContext
import ru.md.base_domain.model.PageResult

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