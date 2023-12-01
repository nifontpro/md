package ru.md.msc.domain.user.biz.validate

import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.parseEmailError
import java.util.regex.Pattern

internal fun ICorChainDsl<UserContext>.validateUserEmailFormat(title: String) = worker {
	this.title = title
	on {
		user.authEmail?.let { email ->
			!isValidEmail(email)
		} ?: false
	}
	handle {
		fail(
			parseEmailError(user.authEmail ?: "")
		)
	}
}

private const val emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"

fun isValidEmail(email: String): Boolean {
	val pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE)
	val matcher = pattern.matcher(email)
	return matcher.matches()
}
