package ru.md.msc.domain.user.model.excel

import ru.md.base_domain.biz.helper.ContextError
import ru.md.msc.domain.user.model.UserDetails

data class AddUserReport(
	val userDetails: UserDetails = UserDetails(),
	val success: Boolean = true,
	val isUpdate: Boolean = false,
	val errors: List<ContextError> = emptyList()
)
