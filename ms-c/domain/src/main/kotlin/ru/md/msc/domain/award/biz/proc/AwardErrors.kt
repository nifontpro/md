package ru.md.msc.domain.award.biz.proc

import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail

fun AwardContext.awardNotFoundError() {
	fail(
		errorDb(
			repository = "award",
			violationCode = "not found",
			description = "Награда не найдена"
		)
	)
}

fun AwardContext.getAwardError() {
	fail(
		errorDb(
			repository = "award",
			violationCode = "get error",
			description = "Ошибка получения награды"
		)
	)
}

class AwardNotFoundException(message: String = "") : RuntimeException(message)
class AlreadyActionException(message: String = "") : RuntimeException(message)
