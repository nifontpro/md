package ru.md.shop.domain.base.biz.proc

import ru.md.base_domain.biz.proc.BaseMedalsContext

abstract class ShopContext : BaseMedalsContext() {
	var productId: Long = 0
}