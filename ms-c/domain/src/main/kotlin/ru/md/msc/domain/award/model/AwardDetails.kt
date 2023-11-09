package ru.md.msc.domain.award.model

import ru.md.base_domain.image.model.BaseImage
import java.time.LocalDateTime

data class AwardDetails(
	val award: Award = Award(),
	val criteria: String? = null,
	val createdAt: LocalDateTime? = null,
	val images: List<BaseImage> = emptyList(),
)