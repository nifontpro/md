package ru.md.msc.db.base.mapper

import org.springframework.data.domain.PageRequest
import ru.md.msc.domain.base.model.BaseQuery

fun BaseQuery.toPageRequest(): PageRequest {
	val page = page
	val pageSize = pageSize
	return PageRequest.of(page, pageSize, orders.toSort())
}

fun String?.toSearch(): String = this?.let { "$it%" } ?: "%"