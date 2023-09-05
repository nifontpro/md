package ru.md.msc.db.award.model.mapper

import ru.md.base_db.mapper.toImage
import ru.md.msc.db.award.model.AwardEntity
import ru.md.msc.db.dept.model.mappers.toDept
import ru.md.msc.db.dept.model.mappers.toDeptEntity
import ru.md.msc.db.dept.model.mappers.toDeptLazy
import ru.md.msc.db.user.model.mappers.toUserLazy
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.dept.model.Dept

fun AwardEntity.toAward() = Award(
	id = id ?: 0,
	name = name,
	type = type,
	mainImg = mainImg,
	startDate = startDate,
	endDate = endDate,
	score = score,
	state = state,
	dept = dept?.toDept() ?: Dept(),
	images = images.map { it.toImage() }
)

fun AwardEntity.toAwardOnlyDept() = Award(
	id = id ?: 0,
	name = name,
	type = type,
	mainImg = mainImg,
	startDate = startDate,
	endDate = endDate,
	score = score,
	state = state,
	dept = dept?.toDeptLazy() ?: Dept(),
)

fun AwardEntity.toAwardOnlyDeptAndUsers() = Award(
	id = id ?: 0,
	name = name,
	type = type,
	mainImg = mainImg,
	startDate = startDate,
	endDate = endDate,
	score = score,
	state = state,
	dept = dept?.toDeptLazy() ?: Dept(),
	users = users.map { it.toUserLazy() }
)

fun AwardEntity.toAwardOnlyImages() = Award(
	id = id ?: 0,
	name = name,
	type = type,
	mainImg = mainImg,
	startDate = startDate,
	endDate = endDate,
	score = score,
	state = state,
	dept = Dept(id = dept?.id ?: 0),
	images = images.map { it.toImage() }
)

fun AwardEntity.toAwardLazy() = Award(
	id = id ?: 0,
	name = name,
	type = type,
	mainImg = mainImg,
	startDate = startDate,
	state = state,
	endDate = endDate,
	score = score,
	dept = Dept(id = dept?.id ?: 0),
)

fun Award.toAwardEntity(create: Boolean = false) = AwardEntity(
	id = if (create) null else id,
	name = name,
	type = type,
	startDate = startDate,
	endDate = endDate,
	score = score,
	dept = dept.toDeptEntity()
)