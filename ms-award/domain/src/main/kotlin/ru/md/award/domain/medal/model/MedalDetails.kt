package ru.md.award.domain.medal.model

import ru.md.base_domain.image.model.BaseImage
import java.time.LocalDateTime

data class MedalDetails(
	val medal: Medal = Medal(),
	val description: String? = null,
	val createdAt: LocalDateTime = LocalDateTime.now(),
	val images: List<BaseImage> = emptyList(),
)
