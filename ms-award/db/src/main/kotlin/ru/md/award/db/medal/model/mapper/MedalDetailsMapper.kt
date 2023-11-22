package ru.md.award.db.medal.model.mapper

import ru.md.award.db.medal.model.MedalDetailsEntity
import ru.md.award.domain.medal.model.MedalDetails
import ru.md.base_db.image.mappers.toBaseImage

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