package ru.md.shop.rest.product.model.request

data class CreateProductRequest(
	val authId: Long = 0,
	val deptId: Long = 0,

	val name: String = "",
	val price: Int = 0,
	val count: Int = 0,
	val description: String? = null,
	val shortDescription: String? = null,
	val place: String? = null,
	val siteUrl: String? = null,
)