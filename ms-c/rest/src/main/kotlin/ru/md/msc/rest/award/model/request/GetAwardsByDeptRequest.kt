package ru.md.msc.rest.award.model.request

import ru.md.base_rest.model.BaseRequest
import ru.md.msc.domain.award.model.AwardState

data class GetAwardsByDeptRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val state: AwardState? = null,
	val withUsers: Boolean = false,
	val baseRequest: BaseRequest = BaseRequest()
)