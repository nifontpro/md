package ru.md.msc.rest.medal.mappers

import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.msc.domain.medal.biz.proc.MedalContext
import ru.md.msc.rest.medal.model.response.MedalDetailsResponse

fun MedalContext.toTransportMedalDetails(): BaseResponse<MedalDetailsResponse> {
	return baseResponse(medalDetails.toMedalDetailsResponse())
}