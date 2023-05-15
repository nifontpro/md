package ru.md.msc.rest.award.model.response

import ru.md.msc.domain.award.model.AwardState
import ru.md.msc.domain.award.model.AwardType
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.image.model.BaseImage

data class AwardResponse(
	val id: Long = 0,
	val name: String = "",
	val type: AwardType = AwardType.UNDEF,
	val startDate: Long? = null,
	val endDate: Long? = null,
	val state: AwardState,
	val dept: Dept = Dept(),
	val images: List<BaseImage> = emptyList()
)
