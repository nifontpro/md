package ru.md.msc.domain.user.model.excel

import ru.md.base_domain.biz.helper.ContextError
import ru.md.msc.domain.event.model.UserEvent
import ru.md.msc.domain.user.model.UserDetails

data class AddUserReport(
	val userDetails: UserDetails? = null,
	val success: Boolean = true,
	val isUpdate: Boolean = false,
	val events: List<UserEvent> = emptyList(),
	val errors: List<ContextError> = emptyList()
)
