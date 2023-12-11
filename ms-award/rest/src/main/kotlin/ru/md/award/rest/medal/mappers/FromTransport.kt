package ru.md.award.rest.medal.mappers

import ru.md.award.domain.medal.biz.proc.MedalCommand
import ru.md.award.domain.medal.biz.proc.MedalContext
import ru.md.award.domain.medal.model.Medal
import ru.md.award.domain.medal.model.MedalDetails
import ru.md.award.rest.medal.model.request.*
import ru.md.base_domain.dept.model.Dept


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
		description = request.description,
		criteria = request.criteria
	)
}

fun MedalContext.fromTransport(request: GetMedalByIdRequest) {
	command = MedalCommand.GET_BY_ID
	authId = request.authId
	medalId = request.medalId
}

fun MedalContext.fromTransport(request: DeleteMedalRequest) {
	command = MedalCommand.DELETE
	authId = request.authId
	medalId = request.medalId
}

fun MedalContext.fromTransport(request: DeleteMedalImageRequest) {
	command = MedalCommand.IMG_DELETE
	authId = request.authId
	medalId = request.medalId
	imageId = request.imageId
}

fun MedalContext.fromTransport(request: AddMedalImageFromGalleryRequest) {
	command = MedalCommand.IMG_ADD_GALLERY
	authId = request.authId
	medalId = request.medalId
	imageId = request.itemId
}
