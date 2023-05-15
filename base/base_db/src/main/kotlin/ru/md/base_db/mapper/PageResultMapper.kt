package ru.md.base_db.mapper

import org.springframework.data.domain.Page
import ru.md.base_domain.model.PageInfo
import ru.md.base_domain.model.PageResult

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