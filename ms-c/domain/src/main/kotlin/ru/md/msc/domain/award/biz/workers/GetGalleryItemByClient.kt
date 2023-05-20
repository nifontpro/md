package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.client.getDataFromMs
import ru.md.base_domain.gallery.request.GetItemByIdRequest
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.getGalleryItemMsError

fun ICorChainDsl<AwardContext>.getGalleryItemByClient(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		val request = GetItemByIdRequest(itemId = imageId)
		smallItem = getDataFromMs(uri = "/gallery/item/get_id", request = request) ?: return@handle
	}

	except {
		getGalleryItemMsError()
	}
}