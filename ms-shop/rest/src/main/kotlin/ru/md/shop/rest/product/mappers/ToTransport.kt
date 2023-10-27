package ru.md.shop.rest.product.mappers

import ru.md.base_domain.model.BaseResponse
import ru.md.base_domain.model.baseResponse
import ru.md.shop.domain.product.biz.proc.ProductContext
import ru.md.shop.domain.product.model.Product
import ru.md.shop.rest.product.model.response.ProductDetailsResponse

fun ProductContext.toTransportProductDetails(): BaseResponse<ProductDetailsResponse> {
	return baseResponse(productDetails.toProductDetailsResponse())
}

fun ProductContext.toTransportProducts(): BaseResponse<List<Product>> {
	return baseResponse(products)
}