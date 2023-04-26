package ru.md.msc.rest.award.mappers

import ru.md.msc.domain.award.biz.proc.AwardCommand
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.rest.award.model.request.CreateAwardRequest
import ru.md.msc.domain.base.model.converter.toLocalDateTimeUTC

fun AwardContext.fromTransport(request: CreateAwardRequest) {
	command = AwardCommand.CREATE
	authId = request.authId
	deptId = request.deptId

	award = Award(
		name = request.name,
		type = request.type,
		startDate = request.startDate.toLocalDateTimeUTC(),
		endDate = request.endDate.toLocalDateTimeUTC(),
		dept = Dept(id = deptId)
	)

	awardDetails = AwardDetails(
		award = award,
		description = request.description,
		criteria = request.criteria,
	)
}