package ru.md.shop.rest.product.model.request

import ru.md.base_rest.model.request.BaseRequest

data class GetProductByCompanyRequest(
	val authId: Long = 0,
	val deptId: Long = 0,
	val maxPrice: Int? = null,
	val available: Boolean = false,
	val baseRequest: BaseRequest = BaseRequest()
)