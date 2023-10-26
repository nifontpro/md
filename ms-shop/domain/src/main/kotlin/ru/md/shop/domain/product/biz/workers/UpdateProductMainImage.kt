package ru.md.shop.domain.product.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.errors.updateMainImageError
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext

fun ICorChainDsl<ProductContext>.updateProductMainImage(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		productService.setMainImage(productId)
	}

	except {
		log.error(it.message)
		updateMainImageError()
	}

}