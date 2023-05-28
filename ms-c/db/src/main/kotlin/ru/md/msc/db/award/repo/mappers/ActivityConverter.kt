package ru.md.msc.db.award.repo.mappers

import ru.md.msc.domain.dept.model.AwardCount
import ru.md.msc.domain.dept.model.IAwardCount

fun IAwardCount.toAwardCount() = AwardCount(
	deptId = getDeptId(),
	deptName = getDeptName(),
	awardCount = getAwardCount(),
	nomineeCount = getNomineeCount()
)