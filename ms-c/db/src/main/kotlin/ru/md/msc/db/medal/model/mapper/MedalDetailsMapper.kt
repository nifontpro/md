package ru.md.msc.db.medal.model.mapper

import ru.md.msc.db.medal.model.MedalDetailsEntity
import ru.md.msc.domain.medal.model.MedalDetails

fun MedalDetails.toMedalDetailsEntity() = MedalDetailsEntity(
	description = description,
	createdAt = createdAt,
	medalEntity = medal.toMedalEntity()
)

fun MedalDetailsEntity.toMedalDetails() = MedalDetails(
	description = description,
	createdAt = createdAt,
	medal = medalEntity.toMedal()
)