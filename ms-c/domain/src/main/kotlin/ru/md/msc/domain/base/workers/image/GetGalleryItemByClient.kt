package ru.md.msc.domain.base.workers.image

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.client.getDataFromMs
import ru.md.base_domain.gallery.request.GetItemByIdRequest
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.base.biz.getGalleryItemMsError

fun <T : BaseClientContext> ICorChainDsl<T>.getGalleryItemByClient(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		val request = GetItemByIdRequest(itemId = imageId)
		smallItem = getDataFromMs(uri = "/item/get_id", request = request) ?: return@handle
	}

	except {
		getGalleryItemMsError()
	}
}