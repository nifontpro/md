package ru.md.shop.domain.pay.biz.proc

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail

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

class UserPayNotFoundException(message: String = "") : RuntimeException(message)
class InsufficientUserBalanceException(message: String = "") : RuntimeException(message)

