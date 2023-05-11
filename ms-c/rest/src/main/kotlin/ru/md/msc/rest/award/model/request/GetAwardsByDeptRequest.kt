package ru.md.msc.rest.award.model.request

import ru.md.msc.domain.award.model.AwardState
import ru.md.msc.rest.base.BaseRequest

data class GetAwardsByDeptRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val state: AwardState? = null,
	val baseRequest: BaseRequest = BaseRequest()
)