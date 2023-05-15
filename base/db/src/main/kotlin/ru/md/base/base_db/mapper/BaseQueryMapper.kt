package ru.md.base.base_db.mapper

import org.springframework.data.domain.PageRequest
import ru.md.base.domain.model.BaseQuery

fun BaseQuery.toPageRequest(): PageRequest {
	val page = page
	val pageSize = pageSize
	return PageRequest.of(page, pageSize, orders.toSort())
}

fun String?.toSearch(): String = this?.let { "$it%" } ?: "%"