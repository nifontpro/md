package ru.md.msc.rest.base.mappers

import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.DEFAULT_PAGE_SIZE
import ru.md.base_domain.model.converter.toLocalDateTimeUTC
import ru.md.msc.rest.base.BaseRequest

fun BaseRequest.toBaseQuery() = BaseQuery(
	page = page ?: 0,
	pageSize = pageSize ?: DEFAULT_PAGE_SIZE,
	filter = filter,
	minDate = minDate?.toLocalDateTimeUTC(),
	maxDate = maxDate?.toLocalDateTimeUTC(),
	orders = orders,
)