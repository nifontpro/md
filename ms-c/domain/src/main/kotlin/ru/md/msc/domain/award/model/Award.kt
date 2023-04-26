package ru.md.msc.domain.award.model

import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.image.model.BaseImage
import java.time.LocalDateTime

data class Award(
	val id: Long? = null,
	var dept: Dept = Dept(),
	var startDate: LocalDateTime? = null,
	var endDate: LocalDateTime? = null,
	var name: String? = null,
	var typeCode: String? = null,

	val images: List<BaseImage> = emptyList()

)