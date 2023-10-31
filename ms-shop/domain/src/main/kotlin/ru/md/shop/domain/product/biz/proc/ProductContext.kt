package ru.md.shop.domain.product.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.shop.domain.base.biz.proc.ShopContext
import ru.md.shop.domain.product.model.Product
import ru.md.shop.domain.product.model.ProductDetails
import ru.md.shop.domain.product.service.ProductService

class ProductContext : ShopContext() {
	var product: Product = Product()
	var productDetails: ProductDetails = ProductDetails()
	var products: List<Product> = emptyList()

	lateinit var productService: ProductService
}

enum class ProductCommand : IBaseCommand {
	CREATE,
	DELETE,
	UPDATE,
	GET_BY_ID,
	GET_BY_COMPANY,
	IMG_ADD,
	IMG_DELETE,
}
