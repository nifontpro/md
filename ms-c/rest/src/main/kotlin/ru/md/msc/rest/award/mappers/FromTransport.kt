package ru.md.msc.rest.award.mappers

import ru.md.msc.domain.award.biz.proc.AwardCommand
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.domain.base.model.converter.toLocalDateTimeUTC
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.rest.award.model.request.*

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

fun AwardContext.fromTransport(request: UpdateAwardRequest) {
	command = AwardCommand.UPDATE
	authId = request.authId

	award = Award(
		id = request.awardId,
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

fun AwardContext.fromTransport(request: GetAwardByIdRequest) {
	command = AwardCommand.GET_BY_ID_DETAILS
	authId = request.authId
	award = Award(id = request.awardId)
}

fun AwardContext.fromTransport(request: GetAwardsByDeptRequest) {
	command = AwardCommand.GET_BY_DEPT
	authId = request.authId
	deptId = request.deptId
	orders = request.orders
}

fun AwardContext.fromTransport(request: DeleteAwardRequest) {
	command = AwardCommand.DELETE
	authId = request.authId
	award = Award(id = request.awardId)
}

fun AwardContext.fromTransport(request: DeleteAwardImageRequest) {
	command = AwardCommand.IMG_DELETE
	authId = request.authId
	imageId = request.imageId
	award = Award(id = request.awardId)
}