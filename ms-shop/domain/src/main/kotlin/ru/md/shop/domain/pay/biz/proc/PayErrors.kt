package ru.md.shop.domain.pay.biz.proc

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.helper.otherError

fun PayContext.userPayNotFoundError() {
	fail(
		errorDb(
			repository = "pay",
			violationCode = "user pay not found",
			description = "У сотрудника не открыт счет",
			level = ContextError.Levels.INFO
		)
	)
}

fun PayContext.getUserPayError() {
	fail(
		errorDb(
			repository = "pay",
			violationCode = "get",
			description = "Ошибка получения баланса счета сотрудника"
		)
	)
}

fun PayContext.getPaysDataError() {
	fail(
		errorDb(
			repository = "pays",
			violationCode = "i/o",
			description = "Ошибка получения платежных данных"
		)
	)
}

fun PayContext.addPayDataError() {
	fail(
		errorDb(
			repository = "pay",
			violationCode = "add",
			description = "Ошибка добавления платежной операции"
		)
	)
}

fun PayContext.insufficientUserBalanceError() {
	fail(
		otherError(
			field = "balance",
			code = "insuff balance",
			description = "На счете недостаточно средств",
			level = ContextError.Levels.INFO
		)
	)
}

class UserPayNotFoundException(message: String = "") : RuntimeException(message)
class PayDataNotFoundException(message: String = "") : RuntimeException(message)
class InsufficientUserBalanceException(message: String = "") : RuntimeException(message)

