package ru.md.shop.domain.product.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.errors.ImageNotFoundException
import ru.md.base_domain.errors.deleteImageError
import ru.md.base_domain.errors.imageNotFoundError
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext

fun ICorChainDsl<ProductContext>.deleteSecondImageFromDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		baseImage = imageService.deleteSecondImage(productId = productId, imageId = imageId)
	}

	except {
		log.info(it.message)
		when (it) {
			is ImageNotFoundException -> imageNotFoundError()
			else -> deleteImageError()
		}
	}
}