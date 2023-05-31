package ru.md.msc.db.award.model.mapper

import ru.md.msc.db.award.model.ActivityEntity
import ru.md.msc.db.dept.model.DeptEntity
import ru.md.msc.db.user.model.mappers.toUser
import ru.md.msc.db.user.model.mappers.toUserEntity
import ru.md.msc.db.user.model.mappers.toUserLazy
import ru.md.msc.domain.award.model.Activity
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.dept.model.Dept

fun ActivityEntity.toActivity() = Activity(
	id = id ?: 0,
	date = date,
	user = user?.toUser(),
	award = award?.toAward(),
	actionType = actionType,
	activ = activ,
	dept = Dept(id = dept?.id ?: 0),
	authId = authId
)

fun ActivityEntity.toActivityOnlyAward() = Activity(
	id = id ?: 0,
	date = date,
	award = award?.toAwardLazy(),
	actionType = actionType,
	activ = activ,
	dept = Dept(id = dept?.id ?: 0),
	authId = authId
)

fun ActivityEntity.toActivityOnlyUser() = Activity(
	id = id ?: 0,
	date = date,
	user = user?.toUser(),
	actionType = actionType,
	activ = activ,
	dept = Dept(id = dept?.id ?: 0),
	authId = authId
)

fun ActivityEntity.toActivityUserLazy() = Activity(
	id = id ?: 0,
	date = date,
	user = user?.toUserLazy(),
	award = award?.toAwardLazy() ?: Award(),
	actionType = actionType,
	activ = activ,
	dept = Dept(id = dept?.id ?: 0),
	authId = authId
)

fun Activity.toActivityEntity(create: Boolean = false) = ActivityEntity(
	id = if (create) null else id,
	date = date,
	user = user?.toUserEntity(),
	award = award?.toAwardEntity(),
	actionType = actionType,
	activ = activ,
	dept = DeptEntity(id = dept?.id),
	authId = authId
)