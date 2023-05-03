package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.addImageError

fun ICorChainDsl<AwardContext>.addAwardImageToDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			baseImage = awardService.addImage(awardId = awardId, baseImage = baseImage)
		} catch (e: Exception) {
			log.error(e.message)
			deleteImageOnFailing = true
			addImageError()
		}
	}
}