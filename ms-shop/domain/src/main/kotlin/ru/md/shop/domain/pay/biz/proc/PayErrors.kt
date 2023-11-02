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

fun PayContext.payProductError() {
	fail(
		errorDb(
			repository = "pay",
			violationCode = "product",
			description = "Ошибка покупки приза"
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
class InsufficientUserBalanceException(message: String = "") : RuntimeException(message)

