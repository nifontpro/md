package ru.md.msc.db.award.model.mapper

import ru.md.msc.db.award.model.ActivityEntity
import ru.md.msc.db.user.model.mappers.toUser
import ru.md.msc.db.user.model.mappers.toUserEntity
import ru.md.msc.db.user.model.mappers.toUserLazy
import ru.md.msc.domain.award.model.Activity
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.user.model.User

fun ActivityEntity.toActivity() = Activity(
	id = id ?: 0,
	date = date,
	user = user?.toUser() ?: User(),
	award = award?.toAward() ?: Award(),
	actionType = actionType,
	activ = activ,
	deptId = deptId,
	authId = authId
)

fun ActivityEntity.toActivityOnlyAward() = Activity(
	id = id ?: 0,
	date = date,
	award = award?.toAwardOnlyImages() ?: Award(),
	actionType = actionType,
	activ = activ,
	deptId = deptId,
	authId = authId
)

fun ActivityEntity.toActivityUserLazy() = Activity(
	id = id ?: 0,
	date = date,
	user = user?.toUserLazy() ?: User(),
	award = award?.toAwardOnlyImages() ?: Award(),
	actionType = actionType,
	activ = activ,
	deptId = deptId,
	authId = authId
)

fun Activity.toActivityEntity(create: Boolean = false) = ActivityEntity(
	id = if (create) null else id,
	date = date,
	user = user?.toUserEntity(),
	award = award?.toAwardEntity(),
	actionType = actionType,
	activ = activ,
	deptId = deptId,
	authId = authId
)