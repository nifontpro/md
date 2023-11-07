package ru.md.msc.db.award.model.mapper

import ru.md.base_db.dept.model.mappers.toDept
import ru.md.base_db.dept.model.mappers.toDeptEntity
import ru.md.base_db.image.mappers.toBaseImage
import ru.md.base_db.user.model.mappers.toUserLazy
import ru.md.base_domain.dept.model.Dept
import ru.md.msc.db.award.model.AwardEntity
import ru.md.msc.domain.award.model.Award

fun AwardEntity.toAward() = Award(
	id = id ?: 0,
	name = name,
	description = description,
	type = type,
	mainImg = mainImg,
	normImg = normImg,
	startDate = startDate,
	endDate = endDate,
	score = score,
	state = state,
	dept = dept?.toDept() ?: Dept(),
	images = images.map { it.toBaseImage() }
)

fun AwardEntity.toAwardOnlyDept() = Award(
	id = id ?: 0,
	name = name,
	description = description,
	type = type,
	mainImg = mainImg,
	normImg = normImg,
	startDate = startDate,
	endDate = endDate,
	score = score,
	state = state,
	dept = dept?.toDept() ?: Dept(),
)

fun AwardEntity.toAwardWithDeptAndUsers() = Award(
	id = id ?: 0,
	name = name,
	description = description,
	type = type,
	mainImg = mainImg,
	normImg = normImg,
	startDate = startDate,
	endDate = endDate,
	score = score,
	state = state,
	dept = dept?.toDept() ?: Dept(),
	users = users.map { it.toUserLazy() }
)

fun AwardEntity.toAwardLazy() = Award(
	id = id ?: 0,
	name = name,
	description = description,
	type = type,
	mainImg = mainImg,
	normImg = normImg,
	startDate = startDate,
	state = state,
	endDate = endDate,
	score = score,
	dept = Dept(id = dept?.id ?: 0),
)

fun Award.toAwardEntity(create: Boolean = false) = AwardEntity(
	id = if (create) null else id,
	name = name,
	description = description,
	type = type,
	startDate = startDate,
	endDate = endDate,
	score = score,
	dept = dept.toDeptEntity()
)