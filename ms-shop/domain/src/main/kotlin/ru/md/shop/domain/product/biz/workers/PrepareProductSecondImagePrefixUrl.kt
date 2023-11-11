package ru.md.shop.domain.product.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext
import ru.md.shop.domain.product.biz.proc.ProductNotFoundException
import ru.md.shop.domain.product.biz.proc.getProductError
import ru.md.shop.domain.product.biz.proc.productNotFoundError

fun ICorChainDsl<ProductContext>.prepareProductSecondImagePrefixUrl(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		deptId = baseProductService.findDeptIdByProductId(productId)
		prefixUrl = "D$deptId/P$productId/Second"
	}

	except {
		log.error(it.message)
		when (it) {
			is ProductNotFoundException -> productNotFoundError()
			else -> getProductError()
		}
	}
}