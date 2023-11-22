package ru.md.msc.domain.base.workers.image

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.client.getData
import ru.md.base_domain.errors.getGalleryItemMsError
import ru.md.base_domain.gallery.request.GetItemByIdRequest
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext


fun <T : BaseClientContext> ICorChainDsl<T>.getGalleryItemTest(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		val request = GetItemByIdRequest(itemId = imageId)
		smallItem = getData(uri = "/item/get", request = request) ?: return@handle
	}

	except {
		log.error(it.message)
		getGalleryItemMsError()
	}
}