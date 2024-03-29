package ru.md.base_rest.model.mapper

import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.DEFAULT_PAGE_SIZE
import ru.md.base_domain.model.mappers.toLocalDateTimeUTC
import ru.md.base_rest.model.request.BaseRequest

fun BaseRequest.toBaseQuery() = BaseQuery(
	page = page ?: 0,
	pageSize = pageSize ?: DEFAULT_PAGE_SIZE,
	filter = filter,
	minDate = minDate?.toLocalDateTimeUTC(),
	maxDate = maxDate?.toLocalDateTimeUTC(),
	subdepts = subdepts,
	orders = orders,
)