package ru.md.shop.domain.product.model

data class Product (
	val id: Long = 0,
	val deptId: Long = 0,
	val name: String = "",
	val price: Int = 0,
	val mainImg: String? = null,
	val normImg: String? = null,
)
