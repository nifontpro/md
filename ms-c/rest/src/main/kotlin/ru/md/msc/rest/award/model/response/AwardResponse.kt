package ru.md.msc.rest.award.model.response

import ru.md.msc.domain.award.model.AwardType
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.image.model.BaseImage

data class AwardResponse(
	val id: Long = 0,
	var name: String = "",
	var type: AwardType = AwardType.UNDEF,
	var startDate: Long? = null,
	var endDate: Long? = null,
	var dept: Dept = Dept(),
	val images: List<BaseImage> = emptyList()
)
