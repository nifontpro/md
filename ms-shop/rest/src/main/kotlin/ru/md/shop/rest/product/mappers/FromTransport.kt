package ru.md.shop.rest.product.mappers

import ru.md.shop.domain.product.biz.proc.ProductCommand
import ru.md.shop.domain.product.biz.proc.ProductContext
import ru.md.shop.domain.product.model.Product
import ru.md.shop.domain.product.model.ProductDetails
import ru.md.shop.rest.product.model.request.*

fun ProductContext.fromTransport(request: CreateProductRequest) {
	command = ProductCommand.CREATE
	authId = request.authId
	deptId = request.deptId

	product = Product(
		name = request.name,
		deptId = deptId,
		price = request.price,
	)

	productDetails = ProductDetails(
		product = product,
		description = request.description,
		siteUrl = request.siteUrl,
	)
}

fun ProductContext.fromTransport(request: DeleteProductRequest) {
	command = ProductCommand.DELETE
	authId = request.authId
	productId = request.productId
}

fun ProductContext.fromTransport(request: UpdateProductRequest) {
	command = ProductCommand.UPDATE
	authId = request.authId
	productId = request.productId

	product = Product(
		id = productId,
		name = request.name,
		deptId = deptId,
		price = request.price,
	)

	productDetails = ProductDetails(
		product = product,
		description = request.description,
		siteUrl = request.siteUrl,
	)
}

fun ProductContext.fromTransport(request: GetProductByIdRequest) {
	command = ProductCommand.GET_BY_ID
	authId = request.authId
	productId = request.productId
}

fun ProductContext.fromTransport(request: GetProductByDeptRequest) {
	command = ProductCommand.GET_BY_DEPT
	authId = request.authId
	deptId = request.deptId
}

fun ProductContext.fromTransport(request: DeleteProductImageRequest) {
	command = ProductCommand.IMG_DELETE
	authId = request.authId
	productId = request.productId
	imageId = request.imageId
}