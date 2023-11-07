package ru.md.msc.rest.dept.mappers

import ru.md.base_domain.model.mappers.toEpochMilliUTC
import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.rest.dept.model.response.DeptDetailsResponse

fun DeptDetails.toDeptDetailsResponse(): DeptDetailsResponse {
	val baseImages = images.map { it.toBaseImageResponse() }
	return DeptDetailsResponse(
		dept = dept.toDeptResponse(baseImages),
		address = address,
		email = email,
		phone = phone,
		description = description,
		createdAt = createdAt?.toEpochMilliUTC(),
		images = baseImages
	)
}