package ru.md.msc.domain.user.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext
import java.util.regex.Pattern

fun ICorChainDsl<UserContext>.validateUserEmailFormat(title: String) = worker {
	this.title = title
	on {
		user.authEmail?.let { email ->
			!isValidEmail(email)
		} ?: false
	}
	handle {
		fail(
			validateUserEmailFormatExt()
		)
	}
}

fun validateUserEmailFormatExt() = errorValidation(
	field = "email",
	violationCode = "not valid",
	description = "Неверный формат email"
)

const val emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"

fun isValidEmail(email: String): Boolean {
	val pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE)
	val matcher = pattern.matcher(email)
	return matcher.matches()
}
