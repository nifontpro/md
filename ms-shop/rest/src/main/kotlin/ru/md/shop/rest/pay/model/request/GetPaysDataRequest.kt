package ru.md.shop.rest.pay.model.request

import ru.md.base_rest.model.request.BaseRequest
import ru.md.shop.domain.pay.model.PayCode

data class GetPaysDataRequest(
	val authId: Long = 0,
	val userId: Long? = null, // Указать, если вызов делает Администратор, null - все сотрудники
	val deptId: Long = 0, // Указать, если вызов делает Владелец
	val payCode: PayCode? = null,
	val isActive: Boolean? = null,
	val baseRequest: BaseRequest = BaseRequest()
)
