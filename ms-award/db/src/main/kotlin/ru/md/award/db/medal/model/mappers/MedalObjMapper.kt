package ru.md.award.db.medal.model.mappers

import ru.md.award.db.medal.model.MedalObjEntity
import ru.md.award.domain.medal.model.MedalObj

fun MedalObjEntity.toMedalObj() = MedalObj(
	id = id ?: 0,
	actId = actId,
	count = count,
	score = score,
	medal = medalEntity.toMedal()
)