package ru.md.msc.rest.award.mappers

import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.AwardCount
import ru.md.msc.domain.award.model.AwardStateCount
import ru.md.msc.domain.award.model.WWAwardCount
import ru.md.msc.rest.award.model.response.ActivityResponse
import ru.md.msc.rest.award.model.response.AwardDetailsResponse
import ru.md.msc.rest.award.model.response.AwardResponse

fun AwardContext.toTransportAwardDetails(): BaseResponse<AwardDetailsResponse> {
	return baseResponse(awardDetails.toAwardDetailsResponse())
}

fun AwardContext.toTransportAwards(): BaseResponse<List<AwardResponse>> {
	return baseResponse(awards.map { it.toAwardResponseWithUsers() })
}

fun AwardContext.toTransportActivity(): BaseResponse<ActivityResponse> {
	return baseResponse(activity.toActivityResponse())
}

fun AwardContext.toTransportActivities(): BaseResponse<List<ActivityResponse>> {
	return baseResponse(activities.map { it.toActivityResponse() })
}

fun AwardContext.toTransportAwardsCount(): BaseResponse<List<AwardCount>> {
	return baseResponse(awardsCount)
}

fun AwardContext.toTransportAwardStateCount(): BaseResponse<AwardStateCount> {
	return baseResponse(awardStateCount)
}

fun AwardContext.toTransportWWAwardsCount(): BaseResponse<WWAwardCount> {
	return baseResponse(wwAwardCount)
}
