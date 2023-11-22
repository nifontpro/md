package ru.md.award.rest.medal.mappers

import ru.md.award.domain.medal.biz.proc.MedalContext
import ru.md.award.rest.medal.model.response.MedalDetailsResponse
import ru.md.base_domain.model.BaseResponse
import ru.md.base_domain.model.baseResponse

fun MedalContext.toTransportMedalDetails(): BaseResponse<MedalDetailsResponse> {
	return baseResponse(medalDetails.toMedalDetailsResponse())
}