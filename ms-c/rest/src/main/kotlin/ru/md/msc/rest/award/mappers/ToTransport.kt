package ru.md.msc.rest.award.mappers

import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.Activity
import ru.md.msc.rest.award.model.response.AwardDetailsResponse
import ru.md.msc.rest.award.model.response.AwardResponse
import ru.md.msc.rest.base.BaseResponse
import ru.md.msc.rest.base.baseResponse

fun AwardContext.toTransportAwardDetails(): BaseResponse<AwardDetailsResponse> {
	return baseResponse(awardDetails.toAwardDetailsResponse())
}

fun AwardContext.toTransportAwards(): BaseResponse<List<AwardResponse>> {
	return baseResponse(awards.map { it.toAwardResponse() })
}

fun AwardContext.toTransportActivity(): BaseResponse<Activity> {
	return baseResponse(activity)
}
