package ru.md.msc.domain.dept.model

import ru.md.base_domain.dept.model.Dept
import ru.md.base_domain.image.model.BaseImage
import java.time.LocalDateTime

data class DeptDetails(
	val dept: Dept = Dept(),
	val address: String? = null,
	val email: String? = null,
	val phone: String? = null,
	val description: String? = null,
	val createdAt: LocalDateTime? = null,
	val images: List<BaseImage> = emptyList(),
)
