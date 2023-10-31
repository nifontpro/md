package ru.md.msc.rest.award.model.response

import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.msc.domain.award.model.AwardState
import ru.md.msc.domain.award.model.AwardType
import ru.md.msc.rest.dept.model.response.DeptResponse
import ru.md.msc.rest.user.model.response.UserResponse

data class AwardResponse(
	val id: Long = 0,
	val name: String = "",
	val description: String? = null,
	val mainImg: String? = null,
	val normImg: String? = null,
	val type: AwardType = AwardType.UNDEF,
	val startDate: Long? = null,
	val endDate: Long? = null,
	val score: Int = 0,
	val state: AwardState,
	val dept: DeptResponse = DeptResponse(),
	val images: List<BaseImageResponse> = emptyList(),
	val users: List<UserResponse> = emptyList(),
)
