package ru.md.shop.domain.product.service

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.shop.domain.product.model.Product
import ru.md.shop.domain.product.model.ProductDetails

interface ProductService {
	fun create(productDetails: ProductDetails): ProductDetails
	fun update(productDetails: ProductDetails): ProductDetails
	fun deleteById(productId: Long)
	fun findProductDetailsById(productId: Long): ProductDetails
	fun addImage(productId: Long, baseImage: BaseImage): BaseImage
	fun setMainImage(productId: Long): BaseImage?
	fun deleteImage(productId: Long, imageId: Long): BaseImage
	fun findByDeptId(
		deptId: Long,
		maxPrice: Int? = null,
		available: Boolean = false,
		baseQuery: BaseQuery
	): PageResult<Product>
}