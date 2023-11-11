package ru.md.shop.domain.product.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.errors.addImageError
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.proc.ProductContext

fun ICorChainDsl<ProductContext>.addProductImageToDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		baseImage = imageService.addImage(productId = productId, baseImage = baseImage)
	}

	except {
		log.error(it.message)
		deleteImageOnFailing = true
		addImageError()
	}

}