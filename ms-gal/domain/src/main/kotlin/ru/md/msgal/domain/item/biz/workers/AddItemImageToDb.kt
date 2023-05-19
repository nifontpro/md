package ru.md.msgal.domain.item.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.base.biz.addImageError
import ru.md.msgal.domain.item.biz.proc.ItemContext

fun ICorChainDsl<ItemContext>.addItemImageToDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			item = itemService.create(galleryItem = item)
		} catch (e: Exception) {
			log.error(e.message)
			deleteImageOnFailing = true
			addImageError()
		}
	}
}