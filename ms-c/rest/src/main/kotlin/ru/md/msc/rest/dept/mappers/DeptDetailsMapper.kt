package ru.md.msc.rest.dept.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.rest.dept.model.response.DeptDetailsResponse

fun DeptDetails.toDeptDetailsResponse() = DeptDetailsResponse(
	dept = dept.toDeptResponse(),
	address = address,
	email = email,
	phone = phone,
	description = description,
	createdAt = createdAt?.toEpochMilliUTC()
)