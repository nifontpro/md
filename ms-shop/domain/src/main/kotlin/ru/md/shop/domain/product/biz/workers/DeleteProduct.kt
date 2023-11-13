package ru.md.shop.domain.product.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.errors.S3DeleteException
import ru.md.base_domain.errors.s3DeleteError
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext
import ru.md.shop.domain.product.biz.proc.ProductNotFoundException
import ru.md.shop.domain.product.biz.proc.productNotFoundError

fun ICorChainDsl<ProductContext>.deleteProduct(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		productDetails = productService.deleteById(productId)
	}

	except {
		log.error(it.message)
		when (it) {
			is ProductNotFoundException -> productNotFoundError()
			is S3DeleteException -> s3DeleteError()
			else -> fail(
				errorDb(
					repository = "product",
					violationCode = "delete",
					description = "Ошибка удаления приза"
				)
			)
		}

	}
}