package ru.md.msc.rest.dept.mappers

import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.rest.dept.model.response.DeptDetailsResponse
import ru.md.msc.rest.user.mappers.toEpochMilli

fun DeptDetails.toDeptDetailsResponse() = DeptDetailsResponse(
	dept = dept,
	address = address,
	email = email,
	phone = phone,
	description = description,
	createdAt = createdAt?.toEpochMilli()
)