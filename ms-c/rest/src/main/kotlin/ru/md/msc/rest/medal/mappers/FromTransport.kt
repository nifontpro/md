package ru.md.msc.rest.medal.mappers

import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.medal.biz.proc.MedalCommand
import ru.md.msc.domain.medal.biz.proc.MedalContext
import ru.md.msc.domain.medal.model.Medal
import ru.md.msc.domain.medal.model.MedalDetails
import ru.md.msc.rest.medal.model.request.CreateMedalRequest
import ru.md.msc.rest.medal.model.request.GetMedalByIdRequest
import ru.md.msc.rest.medal.model.request.UpdateMedalRequest


fun MedalContext.fromTransport(request: CreateMedalRequest) {
	command = MedalCommand.CREATE
	authId = request.authId
	deptId = request.deptId

	medal = Medal(
		name = request.name,
		score = request.score,
		dept = Dept(id = deptId)
	)

	medalDetails = MedalDetails(
		medal = medal,
		description = request.description
	)
}

fun MedalContext.fromTransport(request: UpdateMedalRequest) {
	command = MedalCommand.UPDATE
	authId = request.authId
	medalId = request.medalId

	medal = Medal(
		id = medalId,
		name = request.name,
		score = request.score,
	)

	medalDetails = MedalDetails(
		medal = medal,
		description = request.description
	)
}

fun MedalContext.fromTransport(request: GetMedalByIdRequest) {
	command = MedalCommand.GET_BY_ID_DETAILS
	authId = request.authId
	medalId = request.medalId
}