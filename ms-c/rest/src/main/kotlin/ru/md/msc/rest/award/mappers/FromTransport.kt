package ru.md.msc.rest.award.mappers

import ru.md.base_domain.model.converter.toLocalDateTimeUTC
import ru.md.base_rest.model.mapper.toBaseQuery
import ru.md.msc.domain.award.biz.proc.AwardCommand
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.award.model.AwardDetails
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
		score = request.score,
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
		score = request.score,
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
	awardState = request.state
	withUsers = request.withUsers
	baseQuery = request.baseRequest.toBaseQuery()
}

fun AwardContext.fromTransport(request: GetAwardsBySubDeptsRequest) {
	command = AwardCommand.GET_ADMIN_AVAILABLE
	authId = request.authId
	awardState = request.state
	baseQuery = request.baseRequest.toBaseQuery()
}

fun AwardContext.fromTransport(request: GetAwardsBySubDeptsExcludeUserRequest) {
	command = AwardCommand.GET_ADMIN_AVAILABLE_USER_EXCLUDE
	authId = request.authId
	userId = request.userId
	actionType = request.actionType
	baseQuery = request.baseRequest.toBaseQuery()
}

fun AwardContext.fromTransport(request: GetSimpleAwardsAvailableRequest) {
	command = AwardCommand.GET_SIMPLE_AWARD_AVAILABLE_USER_EXCLUDE
	authId = request.authId
	userId = request.userId
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

fun AwardContext.fromTransport(request: AddAwardImageFromGalleryRequest) {
	command = AwardCommand.IMG_ADD_GALLERY
	authId = request.authId
	awardId = request.awardId
	imageId = request.itemId
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
	awardType = request.awardType
	baseQuery = request.baseRequest.toBaseQuery()
}

fun AwardContext.fromTransport(request: GetActivAwardByDeptRequest) {
	command = AwardCommand.GET_ACTIVE_AWARD_BY_DEPT
	authId = request.authId
	deptId = request.deptId
	awardState = request.awardState
	baseQuery = request.baseRequest.toBaseQuery()
}

fun AwardContext.fromTransport(request: GetUsersByActivAwardRequest) {
	command = AwardCommand.GET_USERS_BY_ACTIVE_AWARD
	authId = request.authId
	awardId = request.awardId
	actionType = request.actionType
	baseQuery = request.baseRequest.toBaseQuery()
}

fun AwardContext.fromTransport(request: GetAwardCountByDeptRequest) {
	command = AwardCommand.COUNT_BY_DEPTS
	authId = request.authId
	deptId = request.deptId
	baseQuery = request.baseRequest.toBaseQuery()
}

fun AwardContext.fromTransport(request: GetActivCountByDeptRequest) {
	command = AwardCommand.COUNT_ACTIV
	authId = request.authId
	deptId = request.deptId
	baseQuery = request.baseRequest.toBaseQuery()
}

fun AwardContext.fromTransport(request: GetActivCountByRootDeptRequest) {
	command = AwardCommand.COUNT_ACTIV_ROOT
	authId = request.authId
	baseQuery = request.baseRequest.toBaseQuery()
}

fun AwardContext.fromTransport(request: GetUsersWWAwardCountByDeptRequest) {
	command = AwardCommand.COUNT_USER_AWARD_WW
	authId = request.authId
	deptId = request.deptId
	baseQuery = request.baseRequest.toBaseQuery()
}

@Suppress("UNUSED_PARAMETER")
fun AwardContext.fromTransport(request: SetMainAwardImagesRequest) {
	command = AwardCommand.SET_MAIN_IMG
}

