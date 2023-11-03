package ru.md.shop.db.product.model.mappers

import ru.md.shop.db.product.model.ProductEntity
import ru.md.shop.domain.product.model.Product

fun Product.toProductEntity() = ProductEntity(
	id = if (id == 0L) null else id,
	deptId = deptId,
	name = name,
	shortDescription = shortDescription,
	price = price,
	count = count,
	mainImg = mainImg,
	normImg = normImg
)

fun ProductEntity.toProduct() = Product(
	id = id ?: 0,
	deptId = deptId,
	name = name,
	shortDescription = shortDescription,
	price = price,
	count = count,
	mainImg = mainImg,
	normImg = normImg
)