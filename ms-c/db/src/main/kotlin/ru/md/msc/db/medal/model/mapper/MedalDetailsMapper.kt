package ru.md.msc.db.medal.model.mapper

import ru.md.base_db.mapper.toBaseImage
import ru.md.msc.db.medal.model.MedalDetailsEntity
import ru.md.msc.domain.medal.model.MedalDetails

fun MedalDetails.toMedalDetailsEntity() = MedalDetailsEntity(
	medalEntity = medal.toMedalEntity(),
	description = description,
	createdAt = createdAt,
)

fun MedalDetailsEntity.toMedalDetails() = MedalDetails(
	medal = medalEntity.toMedal(),
	description = description,
	createdAt = createdAt,
	images = images.map { it.toBaseImage() }
)