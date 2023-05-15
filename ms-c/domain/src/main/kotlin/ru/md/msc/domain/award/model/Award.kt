package ru.md.msc.domain.award.model

import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.image.model.BaseImage
import java.time.LocalDateTime

data class Award(
	val id: Long = 0,
	val name: String = "",
	val type: AwardType = AwardType.UNDEF,
	val startDate: LocalDateTime = LocalDateTime.now(),
	val endDate: LocalDateTime = LocalDateTime.now(),
	val state: AwardState = AwardState.ERROR,
	val dept: Dept = Dept(),
	val images: List<BaseImage> = emptyList()
)