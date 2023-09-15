package ru.md.msc.db.medal.model.mapper

import ru.md.msc.db.medal.model.MedalDetailsEntity
import ru.md.msc.domain.medal.MedalDetails

fun MedalDetails.toMedalDetailEntity() = MedalDetailsEntity(
	medalId = medalId,
	description = description,
	createdAt = createdAt,
	medalEntity = medal.toMedalEntity()
)

fun MedalDetailsEntity.toMedalDetail() = MedalDetails(
	medalId = medalId,
	description = description,
	createdAt = createdAt,
	medal = medalEntity.toMedal()
)