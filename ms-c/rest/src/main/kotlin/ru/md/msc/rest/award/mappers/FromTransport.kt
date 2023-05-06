package ru.md.msc.rest.award.mappers

import ru.md.msc.domain.award.biz.proc.AwardCommand
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.domain.base.model.converter.toLocalDateTimeUTC
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.rest.award.model.request.*
import ru.md.msc.rest.base.mappers.toBaseQuery

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

	awardId = request.awardId
	award = Award(
		id = awardId,
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
	awardId = request.awardId
}

fun AwardContext.fromTransport(request: GetAwardsByDeptRequest) {
	command = AwardCommand.GET_BY_DEPT
	authId = request.authId
	deptId = request.deptId
	baseQuery = request.baseRequest.toBaseQuery()
}

fun AwardContext.fromTransport(request: DeleteAwardRequest) {
	command = AwardCommand.DELETE
	authId = request.authId
	awardId = request.awardId
}

fun AwardContext.fromTransport(request: DeleteAwardImageRequest) {
	command = AwardCommand.IMG_DELETE
	authId = request.authId
	awardId = request.awardId
	imageId = request.imageId
}

fun AwardContext.fromTransport(request: SendActionRequest) {
	command = AwardCommand.ADD_ACTION
	authId = request.authId
	awardId = request.awardId
	userId = request.userId
	actionType = request.actionType
}

fun AwardContext.fromTransport(request: GetActivAwardByUserRequest) {
	command = AwardCommand.GET_ACTIVE_AWARD_BY_USER
	authId = request.authId
	userId = request.userId
	baseQuery = request.baseRequest.toBaseQuery()
}

fun AwardContext.fromTransport(request: GetActivAwardByDeptRequest) {
	command = AwardCommand.GET_ACTIVE_AWARD_BY_DEPT
	authId = request.authId
	deptId = request.deptId
	baseQuery = request.baseRequest.toBaseQuery()
}

fun AwardContext.fromTransport(request: GetUsersByActivAwardRequest) {
	command = AwardCommand.GET_USERS_BY_ACTIVE_AWARD
	authId = request.authId
	awardId = request.awardId
	baseQuery = request.baseRequest.toBaseQuery()
}

