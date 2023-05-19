package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.msc.domain.base.client.getDataFromMs
import ru.md.base_domain.item.SmallItem
import ru.md.base_domain.item.request.GetItemByIdRequest
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext

fun ICorChainDsl<AwardContext>.getGalleryItemByClient(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		val request = GetItemByIdRequest(itemId = imageId)
		val item: SmallItem = getDataFromMs(uri = "/gallery/item/get_id", request = request) ?: return@handle
		println("--------------")
		println(item)
	}
}