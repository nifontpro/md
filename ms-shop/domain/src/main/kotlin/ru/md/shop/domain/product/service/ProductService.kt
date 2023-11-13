package ru.md.shop.domain.product.service

import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.shop.domain.product.model.Product
import ru.md.shop.domain.product.model.ProductDetails

interface ProductService {
	fun create(productDetails: ProductDetails): ProductDetails
	fun update(productDetails: ProductDetails): ProductDetails
	suspend fun deleteById(productId: Long): ProductDetails
	fun findProductDetailsById(productId: Long): ProductDetails
	fun findByDeptId(
		deptId: Long,
		maxPrice: Int? = null,
		available: Boolean = false,
		baseQuery: BaseQuery
	): PageResult<Product>
}