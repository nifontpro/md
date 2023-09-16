package ru.md.msc.domain.medal.biz.proc

//fun AwardContext.awardNotFoundError() {
//	fail(
//		errorDb(
//			repository = "award",
//			violationCode = "not found",
//			description = "Награда не найдена"
//		)
//	)
//}
//
//fun AwardContext.getAwardError() {
//	fail(
//		errorDb(
//			repository = "award",
//			violationCode = "get error",
//			description = "Ошибка получения награды"
//		)
//	)
//}
//
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
