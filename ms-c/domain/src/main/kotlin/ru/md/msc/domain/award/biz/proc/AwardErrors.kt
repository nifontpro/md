package ru.md.msc.domain.award.biz.proc

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail

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

fun AwardContext.getAwardCountError() {
	fail(
		errorDb(
			repository = "award",
			violationCode = "count error",
			description = "Ошибка получения количества наград"
		)
	)
}

fun AwardContext.getActivityError() {
	fail(
		errorDb(
			repository = "activity",
			violationCode = "get error",
			description = "Ошибка получения активности награды"
		)
	)
}

class AwardNotFoundException(message: String = "") : RuntimeException(message)
class AlreadyActionException(message: String = "") : RuntimeException(message)
