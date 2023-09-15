package ru.md.msc.db.medal.model.mapper

import ru.md.msc.db.dept.model.mappers.toDept
import ru.md.msc.db.dept.model.mappers.toDeptEntity
import ru.md.msc.db.medal.model.MedalEntity
import ru.md.msc.domain.medal.Medal

fun Medal.toMedalEntity() = MedalEntity(
	id = if (id == 0L) null else id,
	name = name,
	mainImg = mainImg,
	score = score,
	dept = dept?.toDeptEntity()
)

fun MedalEntity.toMedal()  = Medal(
	id = id ?: 0,
	name = name,
	mainImg = mainImg,
	score = score,
	dept = dept?.toDept()
)