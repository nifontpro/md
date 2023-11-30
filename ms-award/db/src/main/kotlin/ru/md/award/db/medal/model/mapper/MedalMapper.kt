package ru.md.award.db.medal.model.mapper

import ru.md.award.db.medal.model.MedalEntity
import ru.md.award.domain.medal.model.Medal
import ru.md.base_db.dept.model.mappers.toDept
import ru.md.base_db.dept.model.mappers.toDeptEntity
import ru.md.base_domain.dept.model.Dept

fun Medal.toMedalEntity() = MedalEntity(
	id = if (id == 0L) null else id,
	name = name,
	score = score,
	deptEntity = dept.toDeptEntity()
)

fun MedalEntity.toMedalLazy()  = Medal(
	id = id ?: 0,
	name = name,
	mainImg = mainImg,
	normImg = normImg,
	score = score,
	dept = Dept(id = deptEntity?.id ?: 0)
)

fun MedalEntity.toMedal() = Medal(
	id = id ?: 0,
	name = name,
	mainImg = mainImg,
	normImg = normImg,
	score = score,
	dept = deptEntity?.toDept() ?: Dept()
)