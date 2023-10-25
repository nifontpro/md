package ru.md.msc.db.user.model.mappers

import ru.md.msc.db.user.model.IUser
import ru.md.base_domain.dept.model.Dept
import ru.md.base_domain.user.model.User

fun IUser.toUser() = User(
	id = getUserId(),
	firstname = getFirstname(),
	lastname = getLastname(),
	patronymic = getPatronymic(),
	post = getPost(),
	mainImg = getMainImg(),
	awardCount = getAwardCount(),
	scores = getScores(),
	dept = Dept(
		id = getDeptId(),
		name = getDeptName(),
		classname = getClassname(),
	)
)