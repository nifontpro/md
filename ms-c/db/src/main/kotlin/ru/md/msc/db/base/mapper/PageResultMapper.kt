package ru.md.msc.db.base.mapper

import org.springframework.data.domain.Page
import ru.md.msc.domain.base.model.PageInfo
import ru.md.msc.domain.base.model.PageResult

fun <T, R> Page<T>.toPageResult(transform: (T) -> R) = PageResult(
	data = content.map(transform),
	pageInfo = PageInfo(
		pageSize = size,
		pageNumber = number,
		numberOfElements = numberOfElements,
		totalPages = totalPages,
		totalElements = totalElements
	)
)