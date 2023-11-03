package ru.md.shop.domain.product.biz.workers

import ru.md.base_domain.biz.helper.pageFun
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext
import ru.md.shop.domain.product.biz.proc.getProductError

fun ICorChainDsl<ProductContext>.getProductsByDept(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		products = pageFun {
			productService.findByDeptId(
				deptId = deptId,
				maxPrice = maxPrice,
				available = available,
				baseQuery = baseQuery
			)
		}
	}

	except {
		getProductError()
	}
}