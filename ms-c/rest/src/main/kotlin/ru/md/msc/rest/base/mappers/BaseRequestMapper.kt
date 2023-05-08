package ru.md.msc.rest.base.mappers

import ru.md.msc.domain.base.model.BaseQuery
import ru.md.msc.domain.base.model.converter.toLocalDateTimeUTC
import ru.md.msc.rest.base.BaseRequest

fun BaseRequest.toBaseQuery() = BaseQuery(
	page = page,
	pageSize = pageSize,
	filter = filter,
	minDate = minDate?.toLocalDateTimeUTC(),
	maxDate = maxDate?.toLocalDateTimeUTC(),
	orders = orders,
)