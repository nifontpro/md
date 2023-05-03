package ru.md.msc.db.award.model.mapper

import ru.md.msc.db.award.model.AwardEntity
import ru.md.msc.db.base.mapper.toImage
import ru.md.msc.db.dept.model.mappers.toDept
import ru.md.msc.db.dept.model.mappers.toDeptEntity
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.dept.model.Dept

fun AwardEntity.toAward() = Award(
	id = id ?: 0,
	name = name,
	type = type,
	startDate = startDate,
	endDate = endDate,
	dept = dept?.toDept() ?: Dept(),
	images = images.map { it.toImage() }
)

fun AwardEntity.toAwardOnlyImages() = Award(
	id = id ?: 0,
	name = name,
	type = type,
	startDate = startDate,
	endDate = endDate,
	dept = Dept(id = dept?.id ?: 0),
	images = images.map { it.toImage() }
)

fun AwardEntity.toAwardLazy() = Award(
	id = id ?: 0,
	name = name,
	type = type,
	startDate = startDate,
	endDate = endDate,
	dept = Dept(id = dept?.id ?: 0),
)

fun Award.toAwardEntity(create: Boolean = false) = AwardEntity(
	id = if (create) null else id,
	name = name,
	type = type,
	startDate = startDate,
	endDate = endDate,
	dept = dept.toDeptEntity()
)