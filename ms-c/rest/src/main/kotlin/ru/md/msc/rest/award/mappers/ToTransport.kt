package ru.md.msc.rest.award.mappers

import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.rest.award.model.response.ActivityResponse
import ru.md.msc.rest.award.model.response.AwardDetailsResponse
import ru.md.msc.rest.award.model.response.AwardResponse
import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.model.baseResponse

fun AwardContext.toTransportAwardDetails(): BaseResponse<AwardDetailsResponse> {
	return baseResponse(awardDetails.toAwardDetailsResponse())
}

fun AwardContext.toTransportAwards(): BaseResponse<List<AwardResponse>> {
	return baseResponse(awards.map { it.toAwardResponse() })
}

fun AwardContext.toTransportActivity(): BaseResponse<ActivityResponse> {
	return baseResponse(activity.toActivityResponse())
}

fun AwardContext.toTransportActivities(): BaseResponse<List<ActivityResponse>> {
	return baseResponse(activities.map { it.toActivityResponse() })
}
