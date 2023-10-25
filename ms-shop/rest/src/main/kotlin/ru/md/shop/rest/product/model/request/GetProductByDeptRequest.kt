package ru.md.shop.rest.product.model.request

data class GetProductByDeptRequest(
	val authId: Long = 0,
	val deptId: Long = 0
)