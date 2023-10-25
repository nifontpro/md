package ru.md.msc.db.medal.model.mapper

import ru.md.base_db.dept.model.mappers.toDept
import ru.md.base_db.dept.model.mappers.toDeptEntity
import ru.md.msc.db.medal.model.MedalEntity
import ru.md.base_domain.dept.model.Dept
import ru.md.msc.domain.medal.model.Medal

fun Medal.toMedalEntity() = MedalEntity(
	id = if (id == 0L) null else id,
	name = name,
	mainImg = mainImg,
	score = score,
	deptEntity = dept?.toDeptEntity()
)

fun MedalEntity.toMedal()  = Medal(
	id = id ?: 0,
	name = name,
	mainImg = mainImg,
	score = score,
	dept = Dept(id = deptEntity?.id ?: 0)
)

fun MedalEntity.toMedalWithDept() = Medal(
	id = id ?: 0,
	name = name,
	mainImg = mainImg,
	score = score,
	dept = deptEntity?.toDept()
)