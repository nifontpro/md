package ru.md.award.db.activity.model.mappers

import ru.md.award.db.activity.model.ActEntity
import ru.md.award.db.medal.model.mappers.toMedalObj
import ru.md.award.domain.activity.model.Act
import ru.md.base_db.user.model.mappers.toUser

fun ActEntity.toAct() = Act(
	id = id ?: 0,
	name = name,
	actClass = actClass,
	description = description,
	startDate = startDate,
	resultDate = resultDate,
	endDate = endDate,
	medals = medals.map { it.toMedalObj() },
	users = users.map { it.toUser() }
)