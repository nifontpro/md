package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.base.biz.addImageError

fun ICorChainDsl<AwardContext>.addAwardGalleryImageToDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		baseImage = awardService.addGalleryImage(awardId = awardId, smallItem = smallItem)
	}

	except {
		log.error(it.message)
		addImageError()
	}
}