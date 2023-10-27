package ru.md.base_db.base.mapper

import org.springframework.data.domain.PageRequest
import ru.md.base_domain.model.BaseQuery

fun BaseQuery.toPageRequest(): PageRequest {
	val page = page
	val pageSize = pageSize
	return PageRequest.of(page, pageSize, orders.toSort())
}

//fun BaseQuery.toPageRequestNative(): PageRequest {
//	val page = page
//	val pageSize = pageSize
//	return PageRequest.of(page, pageSize, orders.toSortUnsafe())
//}

fun String?.toSearch(): String = this?.let { "$it%" } ?: "%"

fun String?.toSearchOrNull(): String? = this?.let { "$it%" }