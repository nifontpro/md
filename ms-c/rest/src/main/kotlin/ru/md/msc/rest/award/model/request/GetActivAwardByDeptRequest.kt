package ru.md.msc.rest.award.model.request

import ru.md.base_rest.model.request.BaseRequest
import ru.md.msc.domain.award.model.AwardState

data class GetActivAwardByDeptRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val awardState: AwardState? = null, // Фильтр по состоянию награды
	val baseRequest: BaseRequest = BaseRequest()
)