package ru.md.msc.domain.medal.biz.proc

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail

fun MedalContext.medalNotFoundError() {
	fail(
		errorDb(
			repository = "medal",
			violationCode = "not found",
			description = "Медаль не найдена",
			level = ContextError.Levels.INFO
		)
	)
}

fun MedalContext.getMedalError() {
	fail(
		errorDb(
			repository = "medal",
			violationCode = "get error",
			description = "Ошибка получения медали"
		)
	)
}

//fun AwardContext.getAwardCountError() {
//	fail(
//		errorDb(
//			repository = "award",
//			violationCode = "count error",
//			description = "Ошибка получения количества наград"
//		)
//	)
//}
//
//fun AwardContext.getGalleryItemMsError() {
//	fail(
//		otherError(
//			description = "Ошибка получения объекта из микросервиса галереи",
//			field = "gallery",
//			level = ContextError.Levels.ERROR
//		)
//	)
//}
//
//fun AwardContext.getActivityError() {
//	fail(
//		errorDb(
//			repository = "activity",
//			violationCode = "get error",
//			description = "Ошибка получения активности награды"
//		)
//	)
//}

class MedalNotFoundException(message: String = "") : RuntimeException(message)
//class AlreadyActionException(message: String = "") : RuntimeException(message)
