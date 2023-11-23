package ru.md.msc.domain.base.workers.image

import ru.md.base_domain.biz.helper.getBaseData
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.client.getDataFromMs
import ru.md.base_domain.errors.msGetDataError
import ru.md.base_domain.gallery.request.GetItemByIdRequest
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext

fun ICorChainDsl<AwardContext>.getGalleryItemTest(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		smallItem = getBaseData {
			getDataFromMs(
				uri = "/item/get_id",
				request = GetItemByIdRequest(itemId = imageId)
			)
		} ?: return@handle
	}

	except {
		log.error(it.message)
		msGetDataError()
	}
}