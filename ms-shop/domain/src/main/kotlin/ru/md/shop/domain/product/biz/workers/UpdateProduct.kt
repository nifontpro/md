package ru.md.shop.domain.product.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext

fun ICorChainDsl<ProductContext>.updateProduct(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		productDetails = productService.update(productDetails)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "product",
				violationCode = "update",
				description = "Ошибка обновления приза"
			)
		)
	}
}