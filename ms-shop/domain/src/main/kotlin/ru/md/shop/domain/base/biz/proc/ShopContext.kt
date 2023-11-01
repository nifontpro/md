package ru.md.shop.domain.base.biz.proc

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.shop.domain.base.service.BaseProductService

abstract class ShopContext : BaseMedalsContext() {
	var productId: Long = 0

	lateinit var baseProductService: BaseProductService
}